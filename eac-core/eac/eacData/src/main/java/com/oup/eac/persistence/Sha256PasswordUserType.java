package com.oup.eac.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.oup.eac.common.utils.spring.ApplicationContextSupport;
import com.oup.eac.domain.Password;

public class Sha256PasswordUserType implements UserType {

    private StandardPasswordEncoder getPasswordEncoder() {
        return (StandardPasswordEncoder) ApplicationContextSupport.getBean("passwordEncoder");
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class returnedClass() {
        return Password.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { StandardBasicTypes.STRING.sqlType() };
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String md5Password = rs.getString(names[0]);
        if (rs.wasNull())
            return null;
        return new Password(md5Password, true);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, StandardBasicTypes.STRING.sqlType());
        } else {
            Password password = (Password) value;
            String md5;
            if (!password.isHashed()) {
                md5 = getPasswordEncoder().encode(password.getValue());
            } else {
                md5 = password.getValue();
            }
            st.setString(index, md5);
        }
    }
    
}
