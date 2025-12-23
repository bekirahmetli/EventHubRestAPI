package com.example.api;

import com.example.business.abstracts.IUserService;
import com.example.dto.request.user.UserSaveRequest;
import com.example.dto.request.user.UserUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.UserResponse;
import com.example.entities.User;
import com.example.mapper.IModelMapperService;
import com.example.result.Result;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint'ler:
 * - POST    /v1/users        → Kullanıcı oluşturma
 * - GET     /v1/users/{id}   → ID ile kullanıcı getirme
 * - PUT     /v1/users        → Kullanıcı güncelleme
 * - GET     /v1/users        → Sayfalı kullanıcı listeleme
 * - DELETE  /v1/users/{id}   → Kullanıcı silme
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final IUserService userService;
    private final IModelMapperService modelMapperService;

    public UserController(IUserService userService, IModelMapperService modelMapperService) {
        this.userService = userService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<UserResponse> save(@Valid @RequestBody UserSaveRequest request){
        User userToSave = this.modelMapperService.forRequest().map(request,User.class);
        User savedUser = this.userService.save(userToSave);
        UserResponse response = this.modelMapperService.forResponse().map(savedUser,UserResponse.class);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<UserResponse> get(@PathVariable("id") Long id){
        User user = this.userService.get(id);
        UserResponse response = this.modelMapperService.forResponse().map(user,UserResponse.class);
        return ResultHelper.success(response);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<UserResponse> update(@Valid @RequestBody UserUpdateRequest request){
        User userToUpdate = this.modelMapperService.forRequest().map(request, User.class);
        User updatedUser = this.userService.update(userToUpdate);
        UserResponse response = this.modelMapperService.forResponse().map(updatedUser, UserResponse.class);
        return ResultHelper.success(response);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<UserResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize
    ) {
        Page<User> userPage = this.userService.cursor(page, pageSize);
        Page<UserResponse> userResponsePage = userPage.map(user ->
                this.modelMapperService.forResponse().map(user, UserResponse.class)
        );

        return ResultHelper.cursor(userResponsePage);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id){
        this.userService.delete(id);
        return ResultHelper.ok();
    }
}
