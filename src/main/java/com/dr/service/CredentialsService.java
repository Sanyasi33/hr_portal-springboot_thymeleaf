package com.dr.service;

import com.dr.models.db.Credentials;
import com.dr.models.db.Employee;
import com.dr.repository.CredentialsRepository;
import com.dr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmployeeRepository employeeRepo;

    //We need to call this method from main class manually whenever we required to add new HR.
    public void addCredential(Credentials credentials){
        credentials.setPassword(encoder.encode(credentials.getPassword()));
        credentialsRepo.save(credentials);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials c=credentialsRepo.findByUsername(username);
        if (c==null){
            throw new UsernameNotFoundException("User not found");
        }
        return User.builder()
                .username(c.getUsername())
                .password(c.getPassword())
                .roles(c.getRoles().split(","))
                .build();
    }

    //To verify the temp password with emp id
    public String tempPwdVerify(int id, String pwd){
        Optional<Employee> opt=employeeRepo.findById(id);
        if (opt.isPresent()){
            Employee emp=opt.get();
            if (emp.getPassword().equals(pwd)){
                return "valid";
            }
            else
                return "invalid";
        }
        else
            return null;
    }

    //Get employee email from id & then call addCredential() method with email and password
    public void getIdPwd(int id, String pwd){
            Employee emp = employeeRepo.findById(id).get();
            Credentials c = new Credentials();
            c.setUsername(emp.getEmail());
            c.setPassword(pwd);
            c.setRoles("EMPLOYEE");

            addCredential(c);
    }




    //********************************* JWT Related... *************************************//

    //Verify the Username and Password. Then I will generate the JWT token
    public boolean validateUser(String username, String password){
        Credentials c= credentialsRepo.findByUsername(username);
        if (c!=null && encoder.matches(password, c.getPassword())){
            // ^rawPassword, encodePassword^
            return true;
        }
        return false;
    }
}
