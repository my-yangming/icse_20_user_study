package com.sohu.cache.server.data;

import com.sohu.cache.server.data.OSInfo.DistributionType;
import com.sohu.cache.server.data.OSInfo.DistributionVersion;
import com.sohu.cache.server.data.OSInfo.OSType;
import com.sohu.cache.server.data.OSInfo.ProcessorArchitecture;
/**
 * 从OSInfo解�?�?�的OS
 */
public class OS {
	//�?作系统类型
	private OSType osType;
	//�?�行版本
	private DistributionType distributionType;
	//�?�行版本�?�
	private DistributionVersion distributionVersion;
	//处�?�器架构
	private ProcessorArchitecture processorArchitecture;
	
	public OS(OSType osType, DistributionType distributionType,
			DistributionVersion distributionVersion,
			ProcessorArchitecture processorArchitecture) {
		this.osType = osType;
		this.distributionType = distributionType;
		this.distributionVersion = distributionVersion;
		this.processorArchitecture = processorArchitecture;
	}

	public OSType getOsType() {
		return osType;
	}

	public void setOsType(OSType osType) {
		this.osType = osType;
	}

	public DistributionType getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
	}
	
	public DistributionVersion getDistributionVersion() {
		return distributionVersion;
	}

	public void setDistributionVersion(DistributionVersion distributionVersion) {
		this.distributionVersion = distributionVersion;
	}

	public ProcessorArchitecture getProcessorArchitecture() {
		return processorArchitecture;
	}

	public void setProcessorArchitecture(ProcessorArchitecture processorArchitecture) {
		this.processorArchitecture = processorArchitecture;
	}

	@Override
	public String toString() {
		return "OS [osType=" + osType + ", dist="
				+ distributionType + ", version="
				+ distributionVersion + ", bit="
				+ processorArchitecture + "]";
	}
}
