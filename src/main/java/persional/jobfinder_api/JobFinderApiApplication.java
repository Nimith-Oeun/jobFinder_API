package persional.jobfinder_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@Controller
public class JobFinderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobFinderApiApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JobFinderApiApplication.class);
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "JobFinder API");
		String project = "JobFinder (V1.0.0)";
		String dateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT' xxx yyyy"));
		model.addAttribute("project", project);
		model.addAttribute("dateTime", dateTime);
		return "index";
	}

}
