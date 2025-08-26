package es.udc.ws.app.model.util.Match;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlMatchDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlMatchDaoFactory.className";

    private static SqlMatchDao dao = null;

    private SqlMatchDaoFactory(){
    }

    private static SqlMatchDao getInstance() {
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);

            return (SqlMatchDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlMatchDao getDao() {

        if(dao == null){
            dao = getInstance();
        }

        return dao;
    }
}
