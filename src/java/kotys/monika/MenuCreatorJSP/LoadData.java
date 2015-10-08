/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kotys.monika.MenuCreatorJSP;

import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kotys.monika.menucreator.classes.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 *
 * @author oem
 */
public class LoadData extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    private static final long serialVersionUID = 1L;
    private static final String DATA_DIRECTORY = "data";
    private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
    private static final int MAX_REQUEST_SIZE = 1024 * 1024;
    private static MenuCreatorApp instance;
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String redirect = "/Administration/loadFoodCollectionFromCSV.jsp";
        PrintWriter out = response.getWriter();
        try {
            instance = MenuCreatorApp.getInstance();
            FoodComponentCollection_CSVLoader csvLoader = (FoodComponentCollection_CSVLoader) instance.getLoaderByName("FoodComponentCollection CSV Loader");
            File  file = new File (((ArrayList<String>)request.getSession().getAttribute("uploadedFiles")).get(0));
            csvLoader.getDataSourceParameters().put("FilePath", file.getName());
            if(request.getParameter("submitted") == null){
            request.getSession().setAttribute("ObjectName", "Food components");
            request.getSession().setAttribute("headersMapping", csvLoader.getHeadersMaping());
            request.getSession().setAttribute("dataSourceParameters", csvLoader.getDataSourceParameters());
            request.getSession().setAttribute("mapping", csvLoader.getMapping());
            }
            else{
                boolean issues = false;
                for(String key: csvLoader.getHeadersMaping().keySet())
                {
                    String atr = request.getParameter(key);
                    if("".equals(atr))
                        atr = "0";
                    String val = csvLoader.validateHeadersMapping(key, atr);
                    
                    if(!val.isEmpty()){
                        request.setAttribute(key + "_val", val);
                        issues = true;
                    }
                    else
                        csvLoader.getHeadersMaping(). put(key, Integer.parseInt(atr));       
                }
                for(String key: csvLoader.getDataSourceParameters().keySet())
                {
                    String atr = request.getParameter("ds_" + key);
                    if("".equals(atr))
                        atr = "0";
                    String val = csvLoader.validateDataSourceParameters(key, atr);
                    
                    if(!val.isEmpty()){
                        request.setAttribute(key + "_dsval", val);
                        issues = true;
                    }else
                        csvLoader.getDataSourceParameters(). put(key, atr);       
                }
                for(String key: csvLoader.getMapping().keySet())
                {
                    String atr = request.getParameter("ma_" + key);
                    if("".equals(atr))
                        atr = "0";
                    String val = csvLoader.validateMapping(key, atr);
                    
                    if(!val.isEmpty()){
                        request.setAttribute(key + "_mval", val);
                        issues = true;
                    }else
                        csvLoader.getMapping(). put(key, Integer.parseInt(atr));       
                }
                
                 if(!issues){
                     csvLoader.getDataSourceParameters().put("FilePath", file.getPath());
                     if(csvLoader.connect()){
                        csvLoader.loadData();
                        Integer loaded = ((FoodComponentsCollection)csvLoader.getTargetObject()).getFoodList().size();
                        request.setAttribute("loaded", loaded);
                        redirect = "/done.jsp";
                     }
                 }
            }
            
            getServletContext().getRequestDispatcher(redirect).forward(request, response);
        }
        catch (Exception e){
             response.sendError(e.hashCode(), e.getMessage());
        }
        finally {
                
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // Check that we have a file upload request
            request.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        ArrayList<String> files = new ArrayList<String>();
        
        if (!isMultipart) {
            return;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Sets the size threshold beyond which files are written directly to
        // disk.
        factory.setSizeThreshold(MAX_MEMORY_SIZE);

        // Sets the directory used to temporarily store files that are larger
        // than the configured size threshold. We use temporary directory for
        // java
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        // constructs the folder where uploaded file will be stored
        String uploadFolder = getServletContext().getRealPath("")
                + File.separator + DATA_DIRECTORY;

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            // Parse the request
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (!item.isFormField()) {
                    String fileName = new File(item.getName()).getName();
                    String filePath = uploadFolder + File.separator + fileName;
                    File uploadedFile = new File(filePath);
                    System.out.println(filePath);
                    // saves the file to upload directory
                    item.write(uploadedFile);
                    files.add(filePath);
                }
            }

            // displays done.jsp page after upload finished
            request.getSession().setAttribute("uploadedFiles", files);
            processRequest(request, response);

        } catch (FileUploadException ex) {
            throw new ServletException(ex);
        } catch (Exception ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
