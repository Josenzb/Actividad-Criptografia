package com.areandina;


public class SetSQL {
    private static final String INSERT_USUARIO= "INSERT INTO `baseeje3`.`usuarios`" +
            " (`nombre`, `email`, `password`) VALUES ('%s', '%s', '%s');";

    private static final String BUSCAR_USUARIO= "SELECT * FROM `baseeje3`.`usuarios`" +
            "WHERE nombre = '%s';";

    public String preSql(String name, String email, String passwd){
        return String.format(INSERT_USUARIO, name, email, passwd);
    }

    public String preSqlNombre(String name){
        return String.format(BUSCAR_USUARIO, name);
    }

}
