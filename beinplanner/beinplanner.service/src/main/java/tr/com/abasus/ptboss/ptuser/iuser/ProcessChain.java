package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.ptuser.entity.User;

@Component(value="processChain")
@Scope("prototype")
public class ProcessChain {

	@Autowired
	@Qualifier(value="processManager")
	ProcessInterface processInterface;
	
	
	public List<User> findAllInChain(int firmId) {
		List<User> users=processInterface.findAllInChain(firmId);
		return users;
	}
	
	public List<User> findByUserNameAndSurnameInChain(String userName,String userSurname,int firmId) {
		List<User> users=processInterface.findByUserNameAndSurnameInChain(userName,userSurname,firmId);
		return users;
	}


	public ProcessInterface getProcessInterface() {
		return processInterface;
	}


	public void setProcessInterface(ProcessInterface processInterface) {
		this.processInterface = processInterface;
	}
}
