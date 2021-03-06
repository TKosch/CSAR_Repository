package org.opentosca.csarrepo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.opentosca.csarrepo.exception.PersistenceException;
import org.opentosca.csarrepo.model.join.CsarFileOpenToscaServer;
import org.opentosca.csarrepo.model.repository.JoinRepository;

/**
 * Hibernate class for entity CSARFile
 *
 * @author Thomas Kosch (mail@thomaskosch.com), Dennis Przytarski
 */

@Entity
@Table(name = "csar_file")
public class CsarFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "csar_file_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "csar_id")
	private Csar csar;

	@OneToMany(mappedBy = "csarFile")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CloudInstance> cloudInstances = new ArrayList<CloudInstance>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "csarFileOpenToscaServerId.csarFile")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CsarFileOpenToscaServer> csarFileOpenToscaServer = new ArrayList<CsarFileOpenToscaServer>();

	@Column(name = "version")
	private long version;

	@Column(name = "upload_date")
	private Date uploadDate;

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hashed_file_id")
	private HashedFile hashedFile;

	/**
	 * This method maps an OpenTosca instance to the corresponding csarFile in
	 * the database
	 * 
	 * @param openToscaServer
	 *            An OpenToscaServer object instance
	 */
	public void addOpenToscaServer(OpenToscaServer openToscaServer, String location) {
		CsarFileOpenToscaServer csarFileOpenToscaServer = new CsarFileOpenToscaServer(
				new CsarFileOpenToscaServer.CsarFileOpenToscaServerId(this, openToscaServer));

		csarFileOpenToscaServer.setCsarFile(this);
		csarFileOpenToscaServer.setOpenToscaServer(openToscaServer);
		csarFileOpenToscaServer.setLocation(location);

		this.csarFileOpenToscaServer.add(csarFileOpenToscaServer);
	}

	/**
	 * This method removes the mapping of an OpenTosca instance to the csarFile
	 * in the database
	 * 
	 * @param openToscaServer
	 * @throws PersistenceException
	 */
	public void removeOpenToscaServer(OpenToscaServer openToscaServer) throws PersistenceException {
		JoinRepository joinRepository = new JoinRepository();
		joinRepository.removeCsarFileOpenToscaServer(this, openToscaServer);
	}

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id
	 *
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param csar
	 *            the csar to set
	 */
	public void setCsar(Csar csar) {
		this.csar = csar;
	}

	/**
	 * Get CSAR of the current CSAR file
	 *
	 * @return the CSAR
	 */
	public Csar getCsar() {
		return csar;
	}

	/**
	 * Gets the version of a CSAR
	 *
	 * @return version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Sets the version of a CSAR
	 *
	 * @param version
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * Gets the upload date of a CSAR
	 *
	 * @return date of upload
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * Sets the upload date
	 *
	 * @param uploadDate
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the hashedFile
	 */
	public HashedFile getHashedFile() {
		return hashedFile;
	}

	/**
	 * @param hashedFile
	 *            the hashedFile to set
	 */
	public void setHashedFile(HashedFile hashedFile) {
		this.hashedFile = hashedFile;
	}

	/**
	 * @return The cloud instances
	 */
	public List<CloudInstance> getCloudInstances() {
		return cloudInstances;
	}

	/**
	 * @param cloudInstances
	 *            The cloud instances
	 */
	public void setCloudInstances(List<CloudInstance> cloudInstances) {
		this.cloudInstances = cloudInstances;
	}

	/**
	 * @return List containing the correlation of the respective classes
	 */
	public List<CsarFileOpenToscaServer> getCsarFileOpenToscaServer() {
		return csarFileOpenToscaServer;
	}

	/**
	 * @param csarOpenToscaServer
	 *            List containing the correlation of the respective classes
	 */
	public void setCsarFileOpenToscaServer(List<CsarFileOpenToscaServer> csarFileOpenToscaServer) {
		this.csarFileOpenToscaServer = csarFileOpenToscaServer;
	}

}
