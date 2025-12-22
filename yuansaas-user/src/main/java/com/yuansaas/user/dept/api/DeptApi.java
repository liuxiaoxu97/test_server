package com.yuansaas.user.dept.api;

import com.yuansaas.core.response.ResponseBuilder;
import com.yuansaas.core.response.ResponseModel;
import com.yuansaas.user.auth.security.annotations.SecurityAuth;
import com.yuansaas.user.dept.params.FindDeptParam;
import com.yuansaas.user.dept.params.SaveDeptParam;
import com.yuansaas.user.dept.params.UpdateDeptParam;
import com.yuansaas.user.dept.service.DeptService;
import com.yuansaas.user.dept.vo.DeptListVo;
import com.yuansaas.user.dept.vo.DeptTreeListVo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 部门管理Api
 *
 * @author LXZ 2025/10/16 15:05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
public class DeptApi {

    private final DeptService deptService;

    /**
     * 列表查询
     * @param findDeptParam 查询参数
     * @return 部门列表 DeptTreeListVo
     */
    @GetMapping("/list")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<List<DeptTreeListVo>>> list(FindDeptParam findDeptParam) {
        return ResponseBuilder.okResponse(deptService.list(findDeptParam));
    }

    /**
     * 新增部门
     * @param saveDeptParam 新增参数
     * @return 新增结果 true/false
     */
    @PostMapping("/save")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> save(@Validated @RequestBody SaveDeptParam saveDeptParam) {
        return ResponseBuilder.okResponse(deptService.save(saveDeptParam));
    }
    /**
     * 修改部门
     * @param updateDeptParam 修改参数
     * @return 修改结果 true/false
     */
    @PutMapping("/update")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<Boolean>> update(@Validated @RequestBody UpdateDeptParam updateDeptParam) {
        return ResponseBuilder.okResponse(deptService.update(updateDeptParam));
    }
    /**
     * 删除部门
     * @param id 部门id
     * @return 删除结果 true/false
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel<Boolean>> delete(@PathVariable(value = "id") Long id ) {
        return ResponseBuilder.okResponse(deptService.delete(id));
    }
    /**
     * 部门详情
     * @param id 部门id
     * @return 部门详情 DeptListVo
     */
    @GetMapping("/{id}")
    @SecurityAuth(authenticated = false)
    public ResponseEntity<ResponseModel<DeptListVo>> getById(@PathVariable("id") Long id ) {
        return ResponseBuilder.okResponse(deptService.getById("0", id));
    }
}
