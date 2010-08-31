/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.exception.ORMManageException;
import com.clican.pluto.common.util.StringUtils;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

/**
 * This class implement the object-relationship mapping via the POJO and JPA annotation. It will generate the dynamic
 * runtime JAVA codes and compile these codes and load into ClassLoader. After restarting the
 * <code>SessionFactory</code> to take effect.
 * 
 * @author clican
 */
public class DynamicORMManagePojoHibernateImpl implements DynamicORMManage {

    private final static Log log = LogFactory.getLog(DynamicORMManagePojoHibernateImpl.class);

    private SessionFactoryUpdate sessionFactoryUpdate;

    private ModelContainer modelContainer;

    private Template siteTemplate;

    private Template siteDirectoryRelationTemplate;

    private Template directoryTemplate;

    private Template modelTemplate;

    private Template templateTemplate;

    private Template templateDirectoryRelationTemplate;

    private Template templateModelRelationTemplate;

    private String modelDescTempName = "modelDescription";

    private String modelDescListTempName = "modelDescriptionList";

    private String tempORMCfgPojoFolder;

    private DynamicClassLoader dynamicClassLoader;

    private ClassLoaderUtil classLoaderUtil;

    private IDataBaseOperation dataBaseOperation;

    public void setSessionFactoryUpdate(SessionFactoryUpdate sessionFactoryUpdate) {
        this.sessionFactoryUpdate = sessionFactoryUpdate;
    }

    public void setModelContainer(ModelContainer modelContainer) {
        this.modelContainer = modelContainer;
    }

    public void setDirectoryTemplate(Template directoryTemplate) {
        this.directoryTemplate = directoryTemplate;
    }

    public void setModelTemplate(Template modelTemplate) {
        this.modelTemplate = modelTemplate;
    }

    public void setTemplateTemplate(Template templateTemplate) {
        this.templateTemplate = templateTemplate;
    }

    public void setTemplateDirectoryRelationTemplate(Template templateDirectoryRelationTemplate) {
        this.templateDirectoryRelationTemplate = templateDirectoryRelationTemplate;
    }

    public void setTemplateModelRelationTemplate(Template templateModelRelationTemplate) {
        this.templateModelRelationTemplate = templateModelRelationTemplate;
    }

    public void setModelDescTempName(String modelDescTempName) {
        this.modelDescTempName = modelDescTempName;
    }

    public void setTempORMCfgPojoFolder(String tempORMCfgPojoFolder) {
        this.tempORMCfgPojoFolder = tempORMCfgPojoFolder;
    }

    public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
        this.dynamicClassLoader = dynamicClassLoader;
    }

    public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
        this.classLoaderUtil = classLoaderUtil;
    }

    public void setDataBaseOperation(IDataBaseOperation dataBaseOperation) {
        this.dataBaseOperation = dataBaseOperation;
    }

    public void setSiteTemplate(Template siteTemplate) {
        this.siteTemplate = siteTemplate;
    }

    public void setSiteDirectoryRelationTemplate(Template siteDirectoryRelationTemplate) {
        this.siteDirectoryRelationTemplate = siteDirectoryRelationTemplate;
    }

    public void init() throws ORMManageException {
        try {
            dynamicClassLoader
                    .loadClass(Constants.DYNAMIC_MODEL_PACKAGE + "." + Constants.DEFAULT_DIRECTORY_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put(modelDescListTempName, modelContainer.getModelDescs());
            File filePath = new File(tempORMCfgPojoFolder + "/" + Constants.DYNAMIC_MODEL_PACKAGE_PATH);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            generate(filePath.getAbsolutePath() + "/Directory.java", directoryTemplate, velocityContext);
            generate(filePath.getAbsolutePath() + "/Template.java", templateTemplate, velocityContext);
            generate(filePath.getAbsolutePath() + "/TemplateDirectoryRelation.java", templateDirectoryRelationTemplate,
                    velocityContext);
            generate(filePath.getAbsolutePath() + "/Site.java", siteTemplate, velocityContext);
            generate(filePath.getAbsolutePath() + "/SiteDirectoryRelation.java", siteDirectoryRelationTemplate,
                    velocityContext);
            compile(filePath.getAbsolutePath());
            try {
                dynamicClassLoader.refreshClasses();
            } catch (ClassNotFoundException ex) {
                throw new ORMManageException(ex);
            }
            Configuration cfg = sessionFactoryUpdate.update();
            dataBaseOperation.createTable(cfg);
        }
    }

    public void generateORM(ModelDescription modelDescription) throws ORMManageException {
        modelContainer.add(modelDescription);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(modelDescTempName, modelDescription);
        velocityContext.put(modelDescListTempName, modelContainer.getModelDescs());
        File filePath = new File(tempORMCfgPojoFolder + "/" + Constants.DYNAMIC_MODEL_PACKAGE_PATH);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        generate(filePath.getAbsolutePath() + "/" + modelDescription.getFirstCharUpperName() + ".java", modelTemplate,
                velocityContext);
        generate(filePath.getAbsolutePath() + "/Directory.java", directoryTemplate, velocityContext);
        generate(filePath.getAbsolutePath() + "/Template.java", templateTemplate, velocityContext);
        generate(filePath.getAbsolutePath() + "/TemplateDirectoryRelation.java", templateDirectoryRelationTemplate,
                velocityContext);
        generate(filePath.getAbsolutePath() + "/Template" + modelDescription.getFirstCharUpperName() + "Relation.java",
                templateModelRelationTemplate, velocityContext);
        compile(filePath.getAbsolutePath());
        try {
            dynamicClassLoader.refreshClasses();
        } catch (ClassNotFoundException e) {
            throw new ORMManageException(e);
        }
    }

    private void compile(String file) throws ORMManageException {
        Process proc = null;
        try {
            Set<String> jars = classLoaderUtil.getRuntimeJars();
            StringBuffer command = new StringBuffer();
            command.append("javac ");
            command.append(file);
            command.append("/*.java -encoding utf-8 -classpath \"");
            command.append(StringUtils.getSymbolSplitString(jars, ";"));
            command.append("\"");
            if (log.isDebugEnabled()) {
                log.debug("command=" + command);
            }
            proc = Runtime.getRuntime().exec(command.toString());
            int exitValue = -1;
            while (true) {
                try {
                    exitValue = proc.exitValue();
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(10);
                    continue;
                }
                break;
            }
            if (exitValue != 0) {
                log.error("Compile failure command=[" + command + "]");
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Compile successfully.");
                }
            }
        } catch (Exception e) {
            throw new ORMManageException(e);
        } finally {
            proc.destroy();
        }
    }

    private void generate(String file, Template template, VelocityContext velocityContext) throws ORMManageException {
        Writer w = null;
        try {
            w = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            template.merge(velocityContext, w);
            w.flush();
        } catch (Exception e) {
            throw new ORMManageException(e);
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public void updateORM(ModelDescription oldOne, ModelDescription newOne) throws ORMManageException {
        modelContainer.update(oldOne, newOne);
        VelocityContext velocityContext = new VelocityContext();
        if (!oldOne.getName().equals(newOne.getName())) {
            final ModelDescription md = oldOne;
            File filePath = new File(tempORMCfgPojoFolder + "/" + Constants.DYNAMIC_MODEL_PACKAGE_PATH);
            File[] files = filePath.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.contains(md.getFirstCharUpperName());
                }
            });
            for (File f : files) {
                f.delete();
            }
            List<ModelDescription> modelDescriptionList = new ArrayList<ModelDescription>(
                    modelContainer.getModelDescs());
            velocityContext.put(modelDescListTempName, modelDescriptionList);
            generate(filePath.getAbsolutePath() + "/Directory.java", directoryTemplate, velocityContext);
            generate(filePath.getAbsolutePath() + "/Template.java", templateTemplate, velocityContext);
        }
        velocityContext.put(modelDescTempName, newOne);
        File filePath = new File(tempORMCfgPojoFolder + "/" + Constants.DYNAMIC_MODEL_PACKAGE_PATH);
        generate(filePath.getAbsolutePath() + "/" + newOne.getFirstCharUpperName() + ".java", modelTemplate,
                velocityContext);
        compile(filePath.getAbsolutePath());
        try {
            dynamicClassLoader.refreshClasses();
        } catch (ClassNotFoundException e) {
            throw new ORMManageException(e);
        }
    }

    public void deleteORM(ModelDescription modelDescription) throws ORMManageException {
        modelContainer.remove(modelDescription);
        final ModelDescription md = modelDescription;
        File filePath = new File(tempORMCfgPojoFolder + "/" + Constants.DYNAMIC_MODEL_PACKAGE_PATH);
        File[] files = filePath.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.equals(md.getFirstCharUpperName() + ".java")
                        || name.equals(md.getFirstCharUpperName() + ".class")
                        || name.equals("Template" + md.getFirstCharUpperName() + "Relation.java")
                        || name.equals("Template" + md.getFirstCharUpperName() + "Relation.class")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File f : files) {
            f.delete();
        }
        VelocityContext velocityContext = new VelocityContext();
        List<ModelDescription> modelDescriptionList = new ArrayList<ModelDescription>(modelContainer.getModelDescs());
        velocityContext.put(modelDescListTempName, modelDescriptionList);
        generate(filePath.getAbsolutePath() + "/Directory.java", directoryTemplate, velocityContext);
        generate(filePath.getAbsolutePath() + "/Template.java", templateTemplate, velocityContext);
        compile(filePath.getAbsolutePath());
        try {
            dynamicClassLoader.refreshClasses();
        } catch (ClassNotFoundException e) {
            throw new ORMManageException(e);
        }
    }
}

// $Id: DynamicORMManagePojoHibernateImpl.java 708 2010-02-10 04:48:18Z clican $