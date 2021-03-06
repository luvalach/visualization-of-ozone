package cz.muni.fi.sdipr.visualizationofozone.downloading.dto;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import cz.muni.fi.sdipr.visualizationofozone.model.Source;

public class DownloadJobDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date dateOfRequestCreation;
	private String fileName;
	private Source source;

	public DownloadJobDTO(Date dateOfRequestCreation, String fileName, Source source) {
		this.dateOfRequestCreation = dateOfRequestCreation;
		this.fileName = fileName;
		this.source = source;
		this.source.getPhenomenonType().size();
	}

	public DownloadJobDTO(String fileName, Source source) {
		this(Calendar.getInstance().getTime(), fileName, source);
	}

	public Date getDateOfRequestCreation() {
		return dateOfRequestCreation;
	}

	public void setDateOfRequestCreation(Date dateOfRequestCreation) {
		this.dateOfRequestCreation = dateOfRequestCreation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Return absolute URL to file as String. URL is composed from source URL
	 * and file name.
	 * 
	 * @return absolute URL to file
	 */
	public String getUrlAsString() {
		return source.getUrl() + "/" + fileName;
	}

	/**
	 * Return absolute URL to file as URL object. URL is composed from source
	 * URL and file name.
	 * 
	 * @return absolute URL to file
	 * @throws MalformedURLException
	 *             when can't create URL object.
	 */
	public URL getUrl() throws MalformedURLException {
		return new URL(getUrlAsString());
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "DownloadJobDTO [dateOfRequestCreation=" + dateOfRequestCreation + ", fileName=" + fileName + ", url="
				+ this.getUrlAsString() + "]";
	}

}
