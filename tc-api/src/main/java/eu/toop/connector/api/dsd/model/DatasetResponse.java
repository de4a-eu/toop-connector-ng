package eu.toop.connector.api.dsd.model;


import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;

public class DatasetResponse {

  /**
   * Simplified DSD Response Representation
   *
   * @author jerouris at 21.05.2020
   */

  private IParticipantIdentifier m_dpIdentifier;

  private String m_datasetIdentifier;

  private String m_distributionFormat;
  private String m_distributionConforms;
  private String m_distributionMediaType;

  private String m_accessServiceConforms;

  private IDocumentTypeIdentifier m_documentTypeIdentifier;

  public IParticipantIdentifier getDpIdentifier() {
    return m_dpIdentifier;
  }

  public DatasetResponse setDpIdentifier(IParticipantIdentifier m_dpIdentifier) {
    this.m_dpIdentifier = m_dpIdentifier;
    return this;
  }

  public String getDatasetIdentifier() {
    return m_datasetIdentifier;
  }

  public void setDatasetIdentifier(String m_datasetIdentifier) {
    this.m_datasetIdentifier = m_datasetIdentifier;

  }

  public String getDistributionFormat() {
    return m_distributionFormat;
  }

  public void setDistributionFormat(String m_distributionFormat) {
    this.m_distributionFormat = m_distributionFormat;

  }

  public String getDistributionConforms() {
    return m_distributionConforms;
  }

  public void setDistributionConforms(String m_distributionConforms) {
    this.m_distributionConforms = m_distributionConforms;

  }

  public String getDistributionMediaType() {
    return m_distributionMediaType;
  }

  public void setDistributionMediaType(String m_distributionMediaType) {
    this.m_distributionMediaType = m_distributionMediaType;

  }

  public String getAccessServiceConforms() {
    return m_accessServiceConforms;
  }

  public void setAccessServiceConforms(String m_accessServiceConforms) {
    this.m_accessServiceConforms = m_accessServiceConforms;

  }

  public IDocumentTypeIdentifier getDocumentTypeIdentifier() {
    return m_documentTypeIdentifier;
  }

  public void setDocumentTypeIdentifier(
      IDocumentTypeIdentifier m_documentTypeIdentifier) {
    this.m_documentTypeIdentifier = m_documentTypeIdentifier;

  }
}
