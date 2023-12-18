package com.smartparkingupc.utils;

public class ResponseConstants {

  public static final String CREATED_USER_MESSAGE = "Usuario creado con éxito";
  public static final String ISSUE_WHILE_CREATING_MESSAGE = "Tuvimos problemas con tu registro. " +
          "Por favor, intenta más tarde";
  public static final String FOUND_USER_MESSAGE = "Usuario encontrado";
  public static final String ISSUE_WHILE_FINDING_USER = "No hemos encontrado al usuario solicitado" +
          "Revisa las credenciales y vuelve a intentarlo";

  public static final String FOUND_VEHICLES_MESSAGE = "Vehiculos encontrados";
  public static final String ISSUE_WHILE_FINDING_VEHICLES = "No pudimos encontrar tus vehículos." +
          "Por favor, vuelve a intentarlo";


  // ----------------CONFIDENCE CIRCLE CONTROLELR CONSTANTS--------------------
  // --------------------------------------------------------------------------

  public static final String CONFIDENCE_USER_ADDED = "Usuario de confianza añadido";
  public static final String ISSUE_WHILE_ADDING_CONFIDENCE_USER = "No pudimos añadir al usuario de confianza. Intente mas tarde";
  public static final String CONFIDENCE_USER_DELETED = "Usuario de confianza confianza eliminado";
  public static final String ISSUE_WHILE_DELETING_USER = "No pudimo eliminar al usuario de confianza. Intente nuevamente";


  // ----------------------VEHICLE CONTROLLER CONSTANTS------------------------
  // --------------------------------------------------------------------------

  public static final String SAVED_VEHICLE_MESSAGE = "Vehículo almacenado en la base de datos";
  public static final String ISSUE_WHILE_SAVING_VEHICLE = "El vehículo no se encuentra en la base de datos";

  // ----------------------WATCHMAN CONTROLLER CONSTANTS-----------------------
  // --------------------------------------------------------------------------

  public static final String VEHICLE_RELATED_USERS_FOUND = "Usuarios relacionados al vehículo:";

}
