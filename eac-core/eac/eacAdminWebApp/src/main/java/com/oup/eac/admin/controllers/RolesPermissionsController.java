package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;
import com.oup.eac.dto.RoleCriteria;
import com.oup.eac.dto.RoleCriteria.PermissionSelection;
import com.oup.eac.service.RoleService;

@Controller
@RequestMapping("/mvc/roles/manage.htm")
public class RolesPermissionsController {
    
    private static final Logger LOGGER = Logger.getLogger(RolesPermissionsController.class);
    
    private static final String ATTR_MODEL = "roleCriteria";
    
    private static final String ERROR_MSG_KEY  ="errorMsg";
    private static final String SUCCESS_MSG_KEY  ="successMsg";
    
    private static final String MSG_DELETE_FAILED  = "status.role.delete.failed";    
    private static final String MSG_SAVED_SUCCESS  = "status.role.saved.success"; 
    private static final String MSG_UPDATE_SUCCESS  = "status.role.update.success"; 
    private static final String MSG_DELETE_SUCCESS  = "status.role.delete.success"; 
    
    private static final String VIEW_ROLES_PERMISSIONS = "rolesPermissions";
    private static final String VIEW_ROLE_PERMISSIONS = "rolePermissions";
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    @Qualifier("rolePermissionsValidator")
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRoles(@ModelAttribute(ATTR_MODEL) RoleCriteria roleCriteria) {
        return showRoles(roleCriteria, true);
    }
    
    @RequestMapping(method = RequestMethod.GET, params="method=permissions")
    public ModelAndView getRolePermissions(@RequestParam("id")String id) {
        ModelAndView modelAndView = new ModelAndView(VIEW_ROLE_PERMISSIONS);
        modelAndView.addObject("permissions", getPermissionSelection(roleService.getPermissionsByRole(id)));
        modelAndView.addObject("deletable", roleService.isRoleDeleteable(id));
        return modelAndView;
    }
    
    @RequestMapping(method = RequestMethod.GET, params="method=allpermissions")
    public ModelAndView getAllPermissions() {
        ModelAndView modelAndView = new ModelAndView(VIEW_ROLE_PERMISSIONS);
        modelAndView.addObject("permissions", getPermissionSelection(null));
        modelAndView.addObject("deletable", false);
        return modelAndView;
    }
    
    @RequestMapping(method = RequestMethod.POST, params="method=save")
    public ModelAndView saveRole(@ModelAttribute(ATTR_MODEL) RoleCriteria roleCriteria, BindingResult result) {
        validator.validate(roleCriteria, result);
        if(result.hasErrors()) {
            return errorsForm(roleCriteria);
        }
        if((roleCriteria.getRoleId().equals("new"))) {
            return createRole(roleCriteria, result);
        }
        return updateRole(roleCriteria, result);
    }
    
    @RequestMapping(method = RequestMethod.POST, params="method=delete")
    public ModelAndView deleteRole(@ModelAttribute(ATTR_MODEL) RoleCriteria roleCriteria, BindingResult result) {
        try {
            roleService.deleteRole(roleCriteria.getRoleId());
            ModelAndView modelAndView = showRoles(roleCriteria, true);
            modelAndView.addObject(SUCCESS_MSG_KEY, MSG_DELETE_SUCCESS);
            return modelAndView;
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
            ModelAndView modelAndView = showRoles(roleCriteria, false);
            modelAndView.addObject(ERROR_MSG_KEY, MSG_DELETE_FAILED);
            return modelAndView;
        }
    }
    
    private ModelAndView updateRole(RoleCriteria roleCriteria, BindingResult result) {
        try {
            roleService.updateRolePermissions(roleCriteria.getRoleId(), roleCriteria.getRoleName(), getSelected(roleCriteria.getPermissionSelections()));
        } catch (IllegalArgumentException e) {
            result.rejectValue("roleName", "error.role.name.already.in.use", "Name is already in use");
            return errorsForm(roleCriteria);
        }
        return getSuccessForm(roleCriteria, MSG_UPDATE_SUCCESS);
    }
    
    private ModelAndView createRole(@ModelAttribute(ATTR_MODEL) RoleCriteria roleCriteria, BindingResult result) {
        String roleId = roleService.saveRole(roleCriteria.getRoleName(), getSelected(roleCriteria.getPermissionSelections()));
        roleCriteria.setRoleId(roleId);
        return getSuccessForm(roleCriteria, MSG_SAVED_SUCCESS);
    }
    
    private ModelAndView errorsForm(RoleCriteria roleCriteria) {
        ModelAndView modelAndView = new ModelAndView(VIEW_ROLES_PERMISSIONS);
        modelAndView.addObject("roles", roleService.getAllRoles());
        return modelAndView;        
    }

    private ModelAndView getSuccessForm(RoleCriteria roleCriteria, String message) {
        ModelAndView modelAndView = showRoles(roleCriteria, false);
        modelAndView.addObject(SUCCESS_MSG_KEY, message);
        return modelAndView;
    }
    
    private ModelAndView showRoles(final RoleCriteria roleCriteria, final boolean refresh) {
        ModelAndView modelAndView = new ModelAndView(VIEW_ROLES_PERMISSIONS);
        List<Role> roles = roleService.getAllRoles();
        modelAndView.addObject("roles", roles);
        if(roles.size() > 0) {
            if(refresh || StringUtils.isBlank(roleCriteria.getRoleId())) {
                Role role = roles.get(0);
                roleCriteria.setRoleId(role.getId());
                roleCriteria.setRoleName(role.getName());
                roleCriteria.setPermissionSelections(getPermissionSelection(roleService.getPermissionsByRole(role.getId())));
                roleCriteria.setDeletable(roleService.isRoleDeleteable(role.getId()));
            } else {
                roleCriteria.setPermissionSelections(getPermissionSelection(roleService.getPermissionsByRole(roleCriteria.getRoleId())));
                roleCriteria.setDeletable(roleService.isRoleDeleteable(roleCriteria.getRoleId()));
            }
        }
        return modelAndView;
    }
    
    protected String getViewName() {
        return "roles";
    }
    
    private List<PermissionSelection> getPermissionSelection(List<Permission> permissions) {
        List<Permission> allPermissions = roleService.getAllPermissions();
        List<PermissionSelection> list = new ArrayList<PermissionSelection>();
        for(Permission permission : allPermissions) {
            PermissionSelection permissionSelection = new PermissionSelection(permission);
            permissionSelection.setAccess(permissions != null  && permissions.contains(permission));
            list.add(permissionSelection);
        }
        return list;
    }
    
    private Set<String> getSelected(List<PermissionSelection> permissionSelections) {
        Set<String> list = new HashSet<String>();
        for(PermissionSelection permissionSelection : permissionSelections) {
            if(permissionSelection != null && permissionSelection.isAccess()) {
                list.add(permissionSelection.getPermission().getId());
            }
        }
        return list;
    }
}
