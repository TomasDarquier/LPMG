package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.ComponentData;
import org.springframework.stereotype.Service;

@Service
public class DynamicCodeGenerationService {

    public void generateServiceCode(ComponentData componentData,String bucket){
        generateServiceCode(componentData);

        if(componentData.getIsConfigServerEnabled()){
            // generar configs en la base del bucket, al final debe correr en el
            // orchestationService un metodo que recolecte ese archivo y lo inserte
              // en la ubbicacion correspondiente dentro del config server ya generado
            generateDelegatedConfigurations(componentData, bucket);
        }else {
            generateLocalConfigurations(componentData, bucket);
        }
    }

    private void generateLocalConfigurations(ComponentData componentData, String bucket) {

    }

    private void generateDelegatedConfigurations(ComponentData componentData, String bucket) {

    }

    private void generateServiceCode(ComponentData componentData) {

    }

    public void moveConfigsToConfigServer(String projectBucket) {

    }
}
