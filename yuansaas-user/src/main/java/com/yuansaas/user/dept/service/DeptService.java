package com.yuansaas.user.dept.service;

import com.yuansaas.user.dept.params.FindDeptParam;
import com.yuansaas.user.dept.params.SaveDeptParam;
import com.yuansaas.user.dept.params.UpdateDeptParam;
import com.yuansaas.user.dept.vo.DeptListVo;
import com.yuansaas.user.dept.vo.DeptTreeListVo;

import java.util.List;

/**
 *
 * 部门管理
 *
 * @author LXZ 2025/10/17 11:58
 */
public interface DeptService {

    /**
     * 列表查询
     *
     * @param findDeptParam 查询列表信息
     */
    List<DeptTreeListVo> list(FindDeptParam findDeptParam);

    /**
     * 新增部门
     * @param saveDeptParam  保存部门信息
     */
    Boolean save(SaveDeptParam saveDeptParam);
    /**
     * 修改部门
     * @param updateDeptParam 修改部门信息
     */
    Boolean update(UpdateDeptParam updateDeptParam);
    /**
     * 删除部门
     * @param id 部门id
     */
    Boolean delete(Long id );
    /**
     * 部门详情
     * @param merchantCode 商家code
     * @param id 部门id
     */
    DeptListVo getById(String merchantCode,Long id );
}
