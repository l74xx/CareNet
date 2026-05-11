package com.silvercare.service;

import com.silvercare.dao.FamilyLinkDAO;
import com.silvercare.dao.UserDAO;
import com.silvercare.model.FamilyLink;
import com.silvercare.model.User;
import com.silvercare.model.enums.UserRole;

import java.util.List;

public class FamilyService {

    private final FamilyLinkDAO familyLinkDAO = new FamilyLinkDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean linkFamily(int elderId, int familyId, String relationship) {
        User elder = userDAO.findById(elderId);
        User family = userDAO.findById(familyId);

        if (elder == null) {
            System.out.println("找不到長者帳號");
            return false;
        }

        if (family == null) {
            System.out.println("找不到家屬帳號");
            return false;
        }

        if (elder.getRole() != UserRole.ELDER) {
            System.out.println("elderId 必須是長者角色");
            return false;
        }

        if (family.getRole() != UserRole.FAMILY) {
            System.out.println("familyId 必須是家屬角色");
            return false;
        }

        if (familyLinkDAO.exists(elderId, familyId)) {
            System.out.println("此長者與家屬已經綁定");
            return false;
        }

        FamilyLink link = new FamilyLink();
        link.setElderId(elderId);
        link.setFamilyId(familyId);
        link.setRelationship(relationship);

        return familyLinkDAO.create(link);
    }

    public List<FamilyLink> getFamiliesByElderId(int elderId) {
        return familyLinkDAO.findByElderId(elderId);
    }

    public List<FamilyLink> getEldersByFamilyId(int familyId) {
        return familyLinkDAO.findByFamilyId(familyId);
    }
}