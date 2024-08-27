package com.dr.controller;

import com.dr.models.dto.UserDto;
import com.dr.service.CredentialsService;
import com.dr.service.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Tag(name = "Credential Controller", description = "In this controller all the security related APIs developed")
public class CredentialsController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private JWTService jwtService;


    @GetMapping("/")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    //Employee Action
    @Operation(summary = "It will show the temp loin page to enter temp credential")
    @GetMapping("/tempLogin")
    public String tempLogin(){
        return "tempLogin";
        //User will enter empId & pwd then redirect to "/tempLogin" endpoint with POST mode request
    }

    //Employee Action
    @Operation(summary = "Employee verifies the temporary password", description = "Enter empId and password which was sent to your email account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temp credential verified successfully"),
        @ApiResponse(responseCode = "404", description = "Employee Id not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    @PostMapping("/tempLogin")
    public String tempLogin(@RequestParam int empId, @RequestParam String pwd){
        String result=credentialsService.tempPwdVerify(empId, pwd);
        String msg="";
        if (result==null){
            msg= URLEncoder.encode("Invalid Emp Id", StandardCharsets.UTF_8);
            return "redirect:/tempLogin?status=fail&msg="+ msg;
        }
        else{
            if (result.equals("valid")){
                return "redirect:/resetPwd?empId=" + empId;
                //Redirect to "/resetPwd" endpoint with GET mode request
            }
            else {
                msg=URLEncoder.encode("Invalid Password", StandardCharsets.UTF_8);
                return "redirect:/tempLogin?status=fail&msg=" + msg;
            }
        }
    }

    //Employee Action
    @Operation(summary = "It will show the reset password page to enter new password")
    @GetMapping("/resetPwd")
    public String resetPwd(@RequestParam int empId, Model model){
        model.addAttribute("empId", empId);
        return "resetPwd";
        //User will enter new pwd & confirmPwd then redirect to "/resetPwd" endpoint with POST mode request
    }

    //Employee Action
    @Operation(summary = "Employee needs to enter new password", description = "Enter new password and confirm password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "Employee Id not found"),
            @ApiResponse(responseCode = "400", description = "Passwords are not matching")
    })
    @PostMapping("/resetPwd")
    public String resetPwd(@RequestParam int empId, @RequestParam String pwd, @RequestParam String confirmPwd){
        if (pwd.equals(confirmPwd)){
            credentialsService.getIdPwd(empId, pwd);
            return "redirect:/empWelcome";
            //Now redirect to "/empWelcome" endpoint
        }
        else{
            String msg=URLEncoder.encode("Passwords are not matching", StandardCharsets.UTF_8);
            return "redirect:/resetPwd?status=fail&msg=" + msg + "&empId=" + empId;
        }
    }

    @GetMapping("/denied")
    public String denied(){
        return "denied";
    }




    //**************************** JWT Related... ****************************//

    @GetMapping("/loginForToken")
    public String loginForToken(){
        return "loginForToken";
    }

    // Get JWT Token
    @PostMapping("/loginForToken")
    public String getJwtToken(@ModelAttribute UserDto userDto, Model model){
        boolean isValid = credentialsService.validateUser(userDto.getUsername(), userDto.getPassword());
        if(isValid){
            String token = jwtService.generateToken(userDto.getUsername());
            model.addAttribute("token", token);
            return "jwtToken";
        }
        return "redirect:/loginForToken";
    }

}
