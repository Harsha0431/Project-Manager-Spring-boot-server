package com.service;

import com.ApiResponse.ApiResponse;
import com.model.*;
import com.repository.EducationRepository;
import com.repository.SchoolEducationRepository;
import com.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private SchoolEducationRepository schoolEducationEducationRepository;
    @Autowired
    private EducationRepository<UnderGraduation> underGraduationEducationRepository;
    @Autowired
    private EducationRepository<PostGraduation> postGraduationEducationRepository;

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

    // Education details
    public ApiResponse<UserEducation> addUserEducation(UserEducation education, String type){
        ApiResponse<UserEducation> response = new ApiResponse<>();
        try{
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
}
