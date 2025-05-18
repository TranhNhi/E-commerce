package com.Electro.Controller;

import com.Electro.Entities.Invoice;
import com.Electro.Entities.InvoiceDetails;
import com.Electro.Entities.User;
import com.Electro.Exception.NotFoundException;
import com.Electro.Responsitory.UserRepository;
import com.Electro.Service.InvoiceDetailsService;
import com.Electro.Service.InvoiceService;
import com.Electro.payload.request.ChangePassword;
import com.Electro.payload.respone.PageResponse;
import com.Electro.payload.respone.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private InvoiceDetailsService invoiceDetailsService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/all-user")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObject> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.size() > 0){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","get all users successfully", users)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("ok", "cannot find users", "")
        );
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<PageResponse> getAllUsersPage(@RequestParam Optional<Integer> page){
        Page<User> pageUsers = userRepository.findAll(PageRequest.of(page.orElse(0), 6));
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageResponse(page, pageUsers.getSize(), pageUsers.getTotalElements(),
                        pageUsers.getTotalPages(),
                        pageUsers.getContent()
                )
        );
    }

    //    sua
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObject> UpdateUser(@PathVariable Long id, @RequestBody User newUser){
        User existUser = userRepository.findById(id).map((user)->{
            user.setAddress(newUser.getAddress());
            user.setEmail(newUser.getEmail());
            user.setPhone(newUser.getPhone());
            return userRepository.save(user);
        }).orElseGet(()->{
            return userRepository.save(newUser);
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "updated user success", existUser)
        );
    }

    //    xoa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObject> DeleteProduct(@PathVariable Long id){
        boolean existProduct = userRepository.existsById(id);
        if(existProduct){
            userRepository.deleteById(id);
            List<User> users = userRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete user successfully", users)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "cannot find user to delete", "")
        );
    }
    // lấy ra danh sách sản phẩm user đã mua
    @GetMapping("/{userId}/list-product")
    public ResponseEntity<List<InvoiceDetails>> getPurchasedProducts(@PathVariable Long userId) {
        List<InvoiceDetails> purchasedProducts = invoiceDetailsService.getInvoiceDetailsByUserId(userId);
        return ResponseEntity.ok(purchasedProducts);
    }
    @GetMapping("/{userId}/all-invoices")
    public ResponseEntity<List<Invoice>> getUserInvoices(@PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/profile/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    ResponseEntity<ResponseObject> UpdateUserProfile(@PathVariable Long id, @RequestBody User newUser){
        User existUser = userRepository.findById(id).map((user)->{
            user.setAddress(newUser.getAddress());
            user.setEmail(newUser.getEmail());
            user.setPhone(newUser.getPhone());
            return userRepository.save(user);
        }).orElseGet(()->{
            return userRepository.save(newUser);
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "updated user success", existUser)
        );
    }

    @PutMapping("/changePassword/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    ResponseEntity<ResponseObject> ChangePassword(@PathVariable Long id, @RequestBody ChangePassword changePassword){



        Optional<User> existUser = userRepository.findById(id).map((user)->{
            if(!encoder.matches(changePassword.getCurrentPass(), user.getPassword())){
                throw new NotFoundException("old password is not correct");
            }
            user.setPassword(encoder.encode(changePassword.getPassword()));
            return userRepository.save(user);
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "change password user success", existUser)
        );
    }

}