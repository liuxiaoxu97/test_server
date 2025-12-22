package com.yuansaas.user.menu.api;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import com.yuansaas.user.dept.params.FindDeptParam;
import com.yuansaas.user.dept.params.SaveDeptParam;
import com.yuansaas.user.dept.params.UpdateDeptParam;
import com.yuansaas.user.dept.vo.DeptListVo;
import com.yuansaas.user.dept.vo.DeptTreeListVo;
import com.yuansaas.user.menu.params.FindMenuParam;
import com.yuansaas.user.menu.params.SaveMenuParam;
import com.yuansaas.user.menu.params.UpdateMenuParam;
import com.yuansaas.user.menu.service.MenuService;
import com.yuansaas.user.menu.vo.MenuListVo;
import com.yuansaas.user.menu.vo.MenuVo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 菜单
 *
 * @author LXZ 2025/10/21 11:39
 */
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuApi {

    private final MenuService menuService;

    /**
     * 列表查询
     * @param findMenuParam 查询参数
     * @return 菜单列表
     *
     */
    @GetMapping("/list")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<List<MenuListVo>>> list(FindMenuParam findMenuParam) {
        return ResponseBuilder.okResponse(menuService.list(findMenuParam));
    }

    /**
     * 新增菜单
     * @param saveMenuParam 新增参数
     * @return 新增结果
     */
    @PostMapping("/save")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> save(@Validated @RequestBody SaveMenuParam saveMenuParam) {
        return ResponseBuilder.okResponse(menuService.save(saveMenuParam));
    }
    /**
     * 修改菜单
     * @param updateMenuParam 修改参数
     * @return 修改结果
     */
    @PutMapping("/update")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> update(@Validated @RequestBody UpdateMenuParam updateMenuParam) {
        return ResponseBuilder.okResponse(menuService.update(updateMenuParam));
    }
    /**
     * 删除菜单
     * @param id 菜单id
     * @return 删除结果
     */
    @GetMapping("/delete/{id}")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> delete(@PathVariable("id") Long id ) {
        return ResponseBuilder.okResponse(menuService.delete(id));
    }
    /**
     * 禁用菜单
     * @param id 菜单id
     * @return 禁用结果
     */
    @GetMapping("/lock/{id}")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> lock(@PathVariable("id") Long id ) {
        return ResponseBuilder.okResponse(menuService.lock(id));
    }
    /**
     * 菜单详情
     * @param id 菜单id
     * @return 菜单详情
     */
    @GetMapping("/{id}")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<MenuVo>> getById(@PathVariable("id") Long id ) {
        return ResponseBuilder.okResponse(menuService.getById(id));
    }
}
