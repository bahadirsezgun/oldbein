package tr.com.abasus.ptboss.income.entity;

import java.util.List;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;

public class PtMonthlyInOutObj {

	private List<UserBonusPaymentFactory> ubpf;
	
	private List<PtExpenses> ptExp;
	
	private List<PacketPaymentFactory> ppf;

	public List<UserBonusPaymentFactory> getUbpf() {
		return ubpf;
	}

	public void setUbpf(List<UserBonusPaymentFactory> ubpf) {
		this.ubpf = ubpf;
	}

	public List<PtExpenses> getPtExp() {
		return ptExp;
	}

	public void setPtExp(List<PtExpenses> ptExp) {
		this.ptExp = ptExp;
	}

	public List<PacketPaymentFactory> getPpf() {
		return ppf;
	}

	public void setPpf(List<PacketPaymentFactory> ppf) {
		this.ppf = ppf;
	}
	
	
	
}
