package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;

	public PetStoreData savePetStore(PetStoreData petStoreData) {
		
		PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
		copyPetStoreFields(petStore, petStoreData);
		PetStore dbPetStore = petStoreDao.save(petStore);
		return new PetStoreData(dbPetStore);
	}
	
	private void copyPetStoreFields(PetStore petStore, 
			PetStoreData petStoreData) {
	 petStore.setPetStoreId(petStoreData.getPetStoreId());
	 petStore.setStoreName(petStoreData.getStoreName());
	 petStore.setStoreCity(petStoreData.getStoreCity());
	 petStore.setAddress(petStoreData.getAddress());
	 petStore.setCountry(petStoreData.getCountry());
	 }

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		
		return petStoreDao.findById(petStoreId).orElseThrow(
				() -> new NoSuchElementException("Pet Store with ID=" 
		+ petStoreId + " couldn't be found or doesn't exist. Try again."));
	}
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, 
			PetStoreEmployee petStoreEmployee) {
		
		PetStore petStore = findPetStoreById(petStoreId);
		
		Employee employee = findOrCreateEmployee(petStoreId, 
				petStoreEmployee.getEmployeeId());
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		
		return new PetStoreEmployee(employeeDao.save(employee));
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (Objects.isNull(employeeId)) {
			return new Employee();
		}else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(()
				-> new NoSuchElementException("Employee with ID=" + employeeId
						+ " was not found"));
		
		if(employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("EMployee with ID=" + 
		employeeId + " does not work at this pet store with ID=" + petStoreId) ;
		}
		return employee;
	}
	
	private void copyEmployeeFields(Employee employee, 
			PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeName(petStoreEmployee.getEmployeeName());
		employee.setEmployeeEmail(petStoreEmployee.getEmployeeEmail());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		
		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		return new PetStoreCustomer(customerDao.save(customer));
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		}else {
			
		} return findCustomerById(petStoreId, customerId);
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID="
						+ customerId + " was not found."));
		boolean found = false;
		
		for(PetStore petStore : customer.getPetStores()) {
			if(petStore.getPetStoreId() == petStoreId) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("Customer with ID="
					+ customerId + " is not a shopper in this store ID="
					+ petStoreId);
		}
		return customer;
	}

	public List<PetStoreData> retriveAllPetStores() {
		List<PetStoreData> petStoreData = new LinkedList<>();
		
		for(PetStore petStore : petStoreDao.findAll()) {
			PetStoreData data = new PetStoreData(petStore);
			
			data.getCustomers().clear();
			data.getEmployees().clear();
			petStoreData.add(data);
		}
		return petStoreData;
	}
    @Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		
		petStoreDao.delete(petStore);
		
	}

}
