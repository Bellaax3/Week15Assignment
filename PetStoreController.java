package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData createPetStore
	(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		
		return petStoreService.savePetStore(petStoreData);
	}
	@PutMapping("pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployee(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Creating employee {} in store with ID=", 
				petStoreEmployee.getEmployeeId(), petStoreId);
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}
	@PutMapping("/{petStoreId}/employee")
	public PetStoreEmployee updatePetStoreEmployee(@PathVariable Long petStoreId, 
			PetStoreEmployee petStoreEmployee) {
		log.info("Updating employee {} working at pet store with ID=" +
			petStoreEmployee.getEmployeeId(), petStoreId);
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertPetStoreCustomer(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Creating customer {} in store with ID=" +
			petStoreCustomer.getCustomerId(), petStoreId);
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}
	@PutMapping("/{petStoreId}/customer")
	public PetStoreCustomer updatePetStoreCustomer(@PathVariable Long petStoreId,
			PetStoreCustomer petStoreCustomer) {
		log.info("Updating customer {} for pet store ID=" + 
			petStoreCustomer.getCustomerId(), petStoreId);
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}
	
	@GetMapping("/pet_store")
    public List<PetStoreData> listAllPetStores(){
		log.info("Listing all pet stores");
		return petStoreService.retriveAllPetStores();
	}
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving all stores with ID={}" + petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId){
		log.info("Deleting store with ID={}" + petStoreId);
		
		petStoreService.deletePetStoreById(petStoreId);
		
		return Map.of("Message", "Pet Store with ID=" + petStoreId + " deleted successfully");
	}
}
