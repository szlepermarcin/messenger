package pl.szleperm.messenger.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.util.ReflectionTestUtils

import pl.szleperm.messenger.domain.Message
import pl.szleperm.messenger.domain.Role
import pl.szleperm.messenger.domain.User
import pl.szleperm.messenger.repository.MessageRepository
import pl.szleperm.messenger.repository.RoleRepository
import pl.szleperm.messenger.repository.UserRepository
import pl.szleperm.messenger.web.DTO.MessageDTO
import pl.szleperm.messenger.web.DTO.PasswordDTO
import pl.szleperm.messenger.web.DTO.UserDTO
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
class ServiceMethodSecuritySpec extends Specification{
	//shared mocks
	UserRepository userRepository
	RoleRepository roleRepository
	MessageRepository messageRepository
	//services
	@Autowired
	UserService userService
	@Autowired
	MessageService messageService
	//shared data
	User user
	MessageDTO messageDTO
	Message message
	Role role
	static final String USERNAME = "user" 
	static final String OTHER_USERNAME = "other_user"
	static final String PASSWORD = "password"
	static final String EMAIL = "email@email"
	static final String ROLE_USER = "USER"
	static final String ROLE_ADMIN = "ADMIN"
	static final String TITLE = "message title"
	static final String CONTENT = "message content"
	static final Long ID = 10L
	
	def setup(){
		userRepository = Mock(UserRepository)
		roleRepository = Mock(RoleRepository)
		messageRepository = Mock(MessageRepository)
		ReflectionTestUtils.setField(userService,"userRepository", userRepository)
		ReflectionTestUtils.setField(userService,"roleRepository", roleRepository)
		ReflectionTestUtils.setField(messageService,"userRepository", userRepository)
		ReflectionTestUtils.setField(messageService,"messageRepository", messageRepository)
		role = new Role(ROLE_USER)
		user = new User()
		user.setId(ID)
		user.setUsername(USERNAME)
		user.setEmail(EMAIL)
		user.setPassword(PASSWORD)
		user.getRoles().add(role)
		message = new Message(ID, TITLE, CONTENT, USERNAME)
		messageDTO = new MessageDTO(message)
	}
	@WithMockUser(username=USERNAME)
	def "should change password when username match"(){
		setup:
			PasswordDTO passwordDTO = new PasswordDTO()
			passwordDTO.setUsername(USERNAME)
			passwordDTO.setNewPassword(PASSWORD)
		when:
			userService.changePassword(passwordDTO)
		then:
			1 * userRepository.findByUsername(USERNAME) >> Optional.ofNullable(user)
			1 * userRepository.save(_)
		
	}
	@WithMockUser(username=OTHER_USERNAME, roles=ROLE_ADMIN)
	def "should not change password when username doesn't match"(){
		when:
			userService.changePassword(new PasswordDTO())
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.findByUsername(_)
			0 * userRepository.save(_)
		
	}
	@WithAnonymousUser
	def "should not change password when is anonymous"(){
		when:
			userService.changePassword(new PasswordDTO())
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.findByUsername(_)
			0 * userRepository.save(_)
		
	}
	@WithMockUser(roles=ROLE_ADMIN)
	def "should update user when has role ADMIN"(){
		setup:
			UserDTO userDTO = new UserDTO(user)
		when:
			userService.update(userDTO)
		then:
			1 * userRepository.findById(ID) >> Optional.ofNullable(user)
			1 * roleRepository.findByName(role.getName()) >> Optional.of(role)
			1 * userRepository.save(_) 
	}
	@WithMockUser(roles=ROLE_USER)
	def "should not update user when hasn't role ADMIN"(){
		setup:
			UserDTO userDTO = new UserDTO(user)
		when:
			userService.update(userDTO)
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.findById(_) >> Optional.ofNullable(user)
			0 * roleRepository.findByName(_) >> Optional.of(role)
			0 * userRepository.save(_)
	}
	@WithAnonymousUser
	def "should not update user when is anonymous"(){
		setup:
			UserDTO userDTO = new UserDTO(user)
		when:
			userService.update(userDTO)
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.findById(_) >> Optional.ofNullable(user)
			0 * roleRepository.findByName(_) >> Optional.of(role)
			0 * userRepository.save(_)
	}
	@WithMockUser(roles=ROLE_ADMIN)
	def "should delete user when has role ADMIN"(){
		when:
			userService.delete(ID)
		then:
			1 * userRepository.delete(ID)
	}
	@WithMockUser(roles=ROLE_USER)
	def "should not delete user when hasn't role ADMIN"(){
		when:
			userService.delete(ID)
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.delete(_)
	}
	@WithAnonymousUser
	def "should not delete user when anonymous"(){
		when:
			userService.delete(ID)
		then:
			thrown(AccessDeniedException.class)
			0 * userRepository.delete(_)
	}
	@WithMockUser(username=USERNAME, roles=ROLE_USER)
	def "should update message when is author"(){
		when:
			messageService.save(messageDTO)
		then:
			1 * messageRepository.findById(ID) >> Optional.ofNullable(message)
			1 * messageRepository.save(message)
	}
	@WithMockUser(username=OTHER_USERNAME, roles=ROLE_ADMIN)
	def "should update message when is ADMIN"(){
		when:
			messageService.save(messageDTO)
		then:
			1 * messageRepository.findById(ID) >> Optional.ofNullable(message)
			1 * messageRepository.save(message)
	}
	@WithMockUser(username=OTHER_USERNAME, roles=ROLE_USER)
	def "should not update message when isn't author and ADMIN"(){
		when:
			messageService.save(messageDTO)
		then:
			thrown(AccessDeniedException.class)
			0 * messageRepository.findById(_)
			0 * messageRepository.save(_)
	}
	@WithAnonymousUser
	def "should not update message when is anonymous"(){
		when:
			messageService.save(messageDTO)
		then:
			thrown(AccessDeniedException.class)
			0 * messageRepository.findById(_)
			0 * messageRepository.save(_)
	}
	@WithMockUser
	def "should create message when is authenticated"(){
		when:
			messageService.create(messageDTO)
		then:
			1 * messageRepository.save(new Message(messageDTO)) >> message
			1 * userRepository.findByUsername(USERNAME) >> Optional.ofNullable(null)
	}
	@WithAnonymousUser
	def "should not create message when is anonymous"(){
		when:
			messageService.create(messageDTO)
		then:
			thrown(AccessDeniedException.class)
			0 * messageRepository.save(_)
			0 * userRepository.findByUsername(_) 
	}
}