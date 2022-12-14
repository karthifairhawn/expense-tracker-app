package api.v1.controllers.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;

import api.v1.annotations.RestControllers;
import api.v1.contexts.RequestContext;
import api.v1.controllers.RestController;
import api.v1.dto.CommonObjectResponse;
import api.v1.dto.auth.LoginDto;
import api.v1.entity.Users;
import api.v1.exception.CustomException;
import api.v1.service.UsersService;
import api.v1.utils.JsonUtil;
import api.v1.utils.ValidatorUtil;





@RestControllers(path = "/users/{id}")
public class UsersKeyController extends RestController {

       

	private static UsersKeyController usersController;
	public static RestController getInstance() {
		if(usersController == null) usersController = new UsersKeyController();
		return usersController;
	}
	
    public UsersKeyController() {
        super();
    }
    UsersService usersService;
	JsonUtil jsonUtil;
	ValidatorUtil validatorUtil;
	Gson gson;

    public void init(ServletConfig config) throws ServletException {
    	usersService = UsersService.getInstance();
    	validatorUtil = ValidatorUtil.getInstance();
		jsonUtil = JsonUtil.getInstance();
		gson = new Gson();
    }

    								

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Long userId = (Long) ((ArrayList) RequestContext.getAttribute("pathKeys")).get(0);
		
		// User Creation
		Users newUser =  RequestContext.getAttribute("user");
		
		
		// Processing Response
		CommonObjectResponse<Users> jsonResponse = new CommonObjectResponse<Users>(200,newUser);
		resp.setContentType("application/json");
		resp.getWriter().write(gson.toJson(jsonResponse));
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Getting client data
		Users newUser = gson.fromJson( (String)RequestContext.getAttribute("requestBody"), Users.class); 
		
		// User Creation
		newUser =  usersService.update(newUser);
		
		
		// Response Processing
		CommonObjectResponse<Users> responseObject = new CommonObjectResponse<>();
		responseObject.setStatusCode(200);
		responseObject.setData(newUser);
		response.setContentType("application/json");
		response.setStatus(200);
		response.getWriter().write(gson.toJson(responseObject));
	}


	@Override
	public Boolean isValidRequest(String requestBody,String method) {
		
		Long userId = (Long) ((ArrayList) RequestContext.getAttribute("pathKeys")).get(0);
		
		if(method.equals("GET")) {
			Users newUser =  RequestContext.getAttribute("user");
			if(userId != newUser.getId()) {
				throw new CustomException("Invalid user id passed in",401);
			}
		}
		return true;
	}


}
