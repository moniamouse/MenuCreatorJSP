package kotys.monika.MenuCreatorJSP;


import kotys.monika.menucreator.classes.*;
import kotys.monika.menucreator.interfaces.IMenuCreatorLoader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author oem
 */
public class MenuCreatorApp {
    
    private static MenuCreatorApp instance;
    FoodComponentsCollection foodCollection;
    private MenuCreator_LoaderFactory menuLoader;
    //usersList
    //meal collection
    
    public static MenuCreatorApp getInstance(){
        if(instance == null)
            instance = new MenuCreatorApp();
        return instance;
    }
    
    protected MenuCreatorApp(){
        foodCollection = new FoodComponentsCollection("All");
        menuLoader = MenuCreator_LoaderFactory.getInstance();
        //foodCollectionLoadFromSettings
        initalizeLoaders();
    }
    
    public FoodComponentsCollection getFoodCollection(){
        return foodCollection;
    }
    
    private void initalizeLoaders(){
        //TODO: do zrobienia na podstawie pliku inicjalizujacego
        menuLoader.addFoodComponent_Loader(new FoodComponentCollection_CSVLoader());
    }
        
    public IMenuCreatorLoader getLoaderByName(String loaderName){
        if(menuLoader.listAvailbleLoaders().isEmpty())
            initalizeLoaders();
        return menuLoader.findLoaderByName(loaderName);
    }
}