package com.service;

import com.ApiResponse.ApiResponse;
import com.model.*;
import com.repository.EducationOnlyRepository;
import com.repository.EducationRepository;
import com.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private EducationRepository<SchoolEducation> schoolEducationEducationRepository;
    @Autowired
    private EducationRepository<UnderGraduation> underGraduationEducationRepository;
    @Autowired
    private EducationRepository<PostGraduation> postGraduationEducationRepository;
    @Autowired
    private EducationOnlyRepository educationOnlyRepository;
    @Autowired
    private UserService userService;

    public ApiResponse<UserDetails> addUserDetails(UserDetails details){
        ApiResponse<UserDetails> response = new ApiResponse<>();
        try{
            UserDetails savedObject = userDetailsRepository.save(details);
            response.setData(savedObject);
            response.setCode(1);
            response.setMessage("Details saved successfully.");
        }
        catch(Exception e){
            System.out.println("Caught exception in addUserDetails service:" + e.getMessage());
            response.setCode(-1);
            if(e.getMessage().contains("user_details.UK4d9rdl7d52k8x3etihxlaujvh")){
                response.setMessage("User details already saved. Please try to update existing details if needed.");
            }
            else
                response.setMessage("Failed to add save details");
            response.setData(null);
        }
        return response;
    }

    public ApiResponse<UserDetails> updateUserDetails(UserDetails details){
        ApiResponse<UserDetails> response = new ApiResponse<>();
        try{
            UserDetails savedObject = userDetailsRepository.save(details);
            response.setData(savedObject);
            response.setCode(1);
            response.setMessage("Details updated successfully.");
        }
        catch(Exception e){
            System.out.println("Caught exception in updateUserDetails service:" + e.getMessage());
            response.setCode(-1);
            if(e.getMessage().contains("user_details.UK4d9rdl7d52k8x3etihxlaujvh")){
                response.setMessage("User details already saved. Please try to update existing details if needed.");
            }
            else
                response.setMessage("Failed to add update details");
            response.setData(null);
        }
        return response;
    }

    public ApiResponse<UserDetails> getUserDetails(String email){
        ApiResponse<UserDetails> response = new ApiResponse<>();
        try{
            UserDetails details = userDetailsRepository.getUserDetailsWithEmail(email);
            response.setCode(1);
            response.setData(details);
            response.setMessage("User details fetched successfully.");
        }
        catch (Exception e){
            System.out.println("Caught exception in getting user details service: " + e.getMessage());
            response.setCode(-1);
            response.setMessage("Failed to get user details");
            response.setData(null);
        }
        return response;
    }

    // Education details
    public ApiResponse<UserEducation> addUserEducation(UserEducation education, String type, String operation){
        ApiResponse<UserEducation> response = new ApiResponse<>();
        try{
            if(operation.equals("update") && education.getId()==null){
                throw new IllegalAccessException("Invalid education details to update");
            }
            UserEducation savedObject = null;
            if(type.equals("school")){
                savedObject = schoolEducationEducationRepository.save((SchoolEducation) education);
            }
            else if(type.equals("under_graduation"))
                savedObject = underGraduationEducationRepository.save((UnderGraduation) education);
            else if(type.equals("post_graduation"))
                savedObject = postGraduationEducationRepository.save((PostGraduation) education);
            else {
                response.setCode(0);
                response.setMessage("Select education type from school, under graduation and post graduation.");
                response.setData(null);
                return response;
            }
            response.setCode(1);
            response.setData(savedObject);
            response.setMessage("User education details saved successfully");
        }
        catch (Exception e){
            System.out.println("Caught exception in addUserEducation service due to : " + e.getMessage());
            response.setCode(-1);
            if(e.getMessage().contains("education.uk_education"))
                response.setMessage("The combination of education type and course must be unique for a user.");
            else
                response.setMessage("Failed to save education details");
            response.setData(null);
        }
        return response;
    }

    // Delete education entity
    public ApiResponse<String> deleteEducationEntityWithIdAndEmail(Long id, String email){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            UserEducation userEducation = educationOnlyRepository.findById(id).orElse(null);
            if(userEducation == null){
                return new ApiResponse<>(0, "No related education details found to delete.", null);
            }
            if(!userEducation.getUser().getEmail().equals(email)){
                return new ApiResponse<>(-1, "No related education details found to delete.", null);
            }
            educationOnlyRepository.delete(userEducation);
            response.setCode(1);
            response.setMessage("Education details removed successfully.");
            response.setData(null);
        }
        catch (Exception e){
            System.out.println("Caught exception deleteEducationEntityWithIdAndEmail service due to: " + e.getMessage());
            response.setCode(-1);
            response.setMessage("Failed to delete education details.");
            response.setData(null);
        }
        return response;
    }

    // Get education list
    public ApiResponse<List<UserEducation>> getUserEducationList(User user){
        ApiResponse<List<UserEducation>> response = new ApiResponse<>();
        try{
            List<UserEducation> userEducationList = educationOnlyRepository.findByUser(user);
            response.setCode(1);
            response.setMessage("Education details fetched successfully.");
            response.setData(userEducationList);
        }
        catch (Exception e){
            System.out.println("Caught exception getUserEducationList service due to: " + e.getMessage());
            response.setCode(-1);
            response.setMessage("Failed to get education details.");
            response.setData(null);
        }
        return response;
    }
}
