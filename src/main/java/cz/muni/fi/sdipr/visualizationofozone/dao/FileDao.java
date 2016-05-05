package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.Stateless;

import cz.muni.fi.sdipr.visualizationofozone.model.File;

/**
 * DAO for File
 */
@Stateless
public class FileDao extends GenericDao<File, String> {

	public FileDao() {
		super(File.class, String.class);
	}

	public File findByFileName(String fileName) {
		return findById(fileName);
	}
}
