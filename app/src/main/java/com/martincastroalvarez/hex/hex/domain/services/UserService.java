package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.User;
import java.util.List;

public interface UserService {
    User getUser(Integer userId) throws HexagonalEntityNotFoundException;
    List<User> listUsers(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listUsers(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    User updateUser(Integer userId, String name) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    User deactivateUser(int userId) throws HexagonalEntityNotFoundException;
    User activateUser(int userId) throws HexagonalEntityNotFoundException;
    List<User> listManagers(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listManagers(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listProviders(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listProviders(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listSalesmen(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<User> listSalesmen(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    User promoteManager(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
    User demoteManager(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
    User promoteSalesman(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
    User demoteSalesman(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
    User promoteProvider(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
    User demoteProvider(Integer userId) throws HexagonalEntityNotFoundException, HexagonalSecurityException;
}