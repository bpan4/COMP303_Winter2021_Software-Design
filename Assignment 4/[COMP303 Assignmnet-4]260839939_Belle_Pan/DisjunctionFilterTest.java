package comp303assignment4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisjunctionFilterTest {
	
	FilterCollectionDisjunction disjunctionFilter;
	EpisodeNumberFilterStrategy episodeFilter;
	LanguageFilterStrategy languageFilter;
	PublishingStudioFilterStrategy studioFilter;
	
	TVShow showWithPublisher;
	TVShow showWithoutPublisher;
	
	Episode episode1WithPublisher;
	Episode episode1WithoutPublisher;
	Episode episode2WithPublisher;
	Episode episode2WithoutPublisher;
	
	Movie movieWithPublisher;
	Movie movieWithoutPublisher;
	
	List<WatchListFilter> filters;
	
	@BeforeEach
	void setUp() {
		disjunctionFilter = new FilterCollectionDisjunction();
		languageFilter = new LanguageFilterStrategy(Language.ENGLISH);
		studioFilter = new PublishingStudioFilterStrategy("WarnerBrothers");
		episodeFilter = new EpisodeNumberFilterStrategy(1);
		
		showWithPublisher = new TVShow("Yu-Gi-Oh!", Language.ENGLISH, "WarnerBrothers");
		showWithoutPublisher = new TVShow("Yu-Gi-Oh!", Language.ENGLISH, "Konami");
		
		File episodeFile1 = new File("C:\\Users\\user\\Downloads\\YuGiOh1.MOV");
		File episodeFile2 = new File("C:\\Users\\user\\Downloads\\YuGiOh2.MOV");
		
		episode1WithPublisher = showWithPublisher.createAndAddEpisode(episodeFile1, "The Heart of the Cards");
		episode1WithoutPublisher = showWithoutPublisher.createAndAddEpisode(episodeFile1, "The Heart of the Cards");
		
		episode2WithPublisher = showWithPublisher.createAndAddEpisode(episodeFile2, "The Gauntlet Is Thrown");
		episode2WithoutPublisher = showWithoutPublisher.createAndAddEpisode(episodeFile2, "The Gauntlet Is Thrown");
		
		File movieFile = new File("C:\\Users\\user\\Downloads\\YuGiOhMovie.MOV");
		movieWithPublisher = new Movie(movieFile, "The Yu-Gi-Oh! Movie", Language.ENGLISH, "WarnerBrothers");
		movieWithoutPublisher = new Movie(movieFile, "The Yu-Gi-Oh! Movie", Language.ENGLISH, "Konami");
	}
	
	@Test
	void testGetFiltersWhenEmpty() {
		try {
			disjunctionFilter.getFilters();
			fail();
		} catch (AssertionError e) {
			assertEquals(1,1);
		}
	}
	
	@Test
	void testGetFiltersWhenNotEmpty() {
		try {
			List<WatchListFilter> supposedlyAddedFilters = new ArrayList<>();
			disjunctionFilter.addFilter(studioFilter);
			supposedlyAddedFilters.add(studioFilter);
			
			List<WatchListFilter> addedFilters = disjunctionFilter.getFilters();
			assertEquals(supposedlyAddedFilters, addedFilters);
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testNoFiltersAppliedforMovies() {
		try {
			disjunctionFilter.filter(movieWithPublisher);
			fail();
		} catch (AssertionError e) {
			assertEquals(1,1);
		}
	}
	
	@Test
	void testNoFiltersAppliedforTVShows() {
		try {
			disjunctionFilter.filter(showWithPublisher);
			fail();
		} catch (AssertionError e) {
			assertEquals(1,1);
		}
	}
	
	@Test
	void testNoFiltersAppliedforEpisodes() {
		try {
			disjunctionFilter.filter(episode1WithPublisher);
			fail();
		} catch (AssertionError e) {
			assertEquals(1,1);
		}
	}
	
	@Test
	void testAddingFilter() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			filters.add(studioFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testAddingMultipleFilters() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			disjunctionFilter.addFilter(episodeFilter);
			filters.add(studioFilter);
			filters.add(episodeFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testAddingSameFilterTwice() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			disjunctionFilter.addFilter(studioFilter);
			filters.add(studioFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testRemovingFilterWhenItExists() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			disjunctionFilter.removeFilter(studioFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testRemovingMultipleFilters() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			filters.add(studioFilter);
			disjunctionFilter.addFilter(episodeFilter);
			disjunctionFilter.removeFilter(episodeFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testRemovingSameFilterTwice() {
		filters = new ArrayList<>();
		try {
			disjunctionFilter.addFilter(studioFilter);
			disjunctionFilter.removeFilter(studioFilter);
			disjunctionFilter.removeFilter(studioFilter);
			assertEquals(filters, disjunctionFilter.getFilters());
		} catch (AssertionError e) {
			fail();
		}
	}
	
	@Test
	void testMovieDisjunctionFilterUsingOneFilterThatMatches() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(true, disjunctionFilter.filter(movieWithPublisher));
	}
	
	@Test
	void testMovieDisjunctionFilterUsingOneFilterThatDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(false, disjunctionFilter.filter(movieWithoutPublisher));
	}
	
	@Test
	void testMovieDisjunctionFilterUsingMultipleFiltersAndMatchesOne() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(true, disjunctionFilter.filter(movieWithPublisher));
	}
	
	@Test
	void testMovieDisjunctionFilterUsingMultipleFiltersAndDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(false, disjunctionFilter.filter(movieWithoutPublisher));
	}
	
	@Test
	void testMovieDisjunctionFilterUsingMultipleFiltersAndMatchesAll() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(languageFilter);
		assertEquals(true, disjunctionFilter.filter(movieWithPublisher));
	}
	
	@Test
	void testTVShowDisjunctionFilterUsingOneFilterThatMatches() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(true, disjunctionFilter.filter(showWithPublisher));
	}
	
	@Test
	void testTVShowDisjunctionFilterUsingOneFilterThatDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(false, disjunctionFilter.filter(showWithoutPublisher));
	}
	
	@Test
	void testTVShowDisjunctionFilterUsingMultipleFiltersAndMatchesOne() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(true, disjunctionFilter.filter(showWithPublisher));
	}
	
	@Test
	void testTVShowDisjunctionFilterUsingMultipleFiltersAndDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(false, disjunctionFilter.filter(showWithoutPublisher));
	}
	
	@Test
	void testTVShowDisjunctionFilterUsingMultipleFiltersAndMatchesAll() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(languageFilter);
		assertEquals(true, disjunctionFilter.filter(showWithPublisher));
	}
	
	@Test
	void testEpisodeDisjunctionFilterUsingOneFilterThatMatches() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(true, disjunctionFilter.filter(episode1WithPublisher));
	}
	
	@Test
	void testEpisodeDisjunctionFilterUsingOneFilterThatDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		assertEquals(false, disjunctionFilter.filter(episode1WithoutPublisher));
	}
	
	@Test
	void testEpisodeDisjunctionFilterUsingMultipleFiltersAndMatchesOne() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(true, disjunctionFilter.filter(episode1WithoutPublisher));
	}
	
	@Test
	void testEpisodeDisjunctionFilterUsingMultipleFiltersAndDoesNotMatch() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(episodeFilter);
		assertEquals(false, disjunctionFilter.filter(episode2WithoutPublisher));
	}
	
	@Test
	void testEpisodeDisjunctionFilterUsingMultipleFiltersAndMatchesAll() {
		disjunctionFilter.addFilter(studioFilter);
		disjunctionFilter.addFilter(languageFilter);
		assertEquals(true, disjunctionFilter.filter(episode1WithPublisher));
	}
}
