package com.chiriacd.venuefinder;

import com.chiriacd.venuefinder.foursquare.FoursquareService;
import com.chiriacd.venuefinder.foursquare.FoursquareServiceWrapper;
import com.chiriacd.venuefinder.foursquare.api.Group;
import com.chiriacd.venuefinder.foursquare.api.GroupItem;
import com.chiriacd.venuefinder.foursquare.api.VenueRecommendations;
import com.chiriacd.venuefinder.foursquare.api.local.KnownGroupTypes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FoursquareServiceWrapperTests {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock private FoursquareService service;

    private VenueRecommendations venueRecommendations;
    private VenueRecommendations.Response venueRecommendationsResponse;
    private Group recommendedGroup;
    private Group otherGroup;
    private GroupItem.Venue venue;
    private GroupItem recommendedGroupItem;

    private FoursquareServiceWrapper serviceWrapper;
    private GroupItem otherGroupItem;

    @Before
    public void setUp() {
        serviceWrapper = new FoursquareServiceWrapper(service);
    }

    @Test
    public void givenTwoGroupsAreInResponse_whenGetVenuesByType_onlyVenueFromRecommendedGroupIsEmitted() throws Exception {
        setUpResponseWith2Groups();

        List<GroupItem> recommendedGroupItemList = new ArrayList<>();
        List<GroupItem> otherGroupList = new ArrayList<>();

        recommendedGroupItemList.add(recommendedGroupItem);
        otherGroupList.add(otherGroupItem);

        when(recommendedGroupItem.getVenue()).thenReturn(venue);

        when(recommendedGroup.getItems()).thenReturn(recommendedGroupItemList);
        when(recommendedGroup.getName()).thenReturn(KnownGroupTypes.RECOMMENDED.value);

        when(otherGroup.getItems()).thenReturn(otherGroupList);
        when(otherGroup.getName()).thenReturn("unexpectedGroupType");

        when(service.getRecommendedVenues(any())).thenReturn(Observable.just(venueRecommendations));

        serviceWrapper.getVenuesByType("anything", KnownGroupTypes.RECOMMENDED)
                .test()
                .assertNoErrors()
                .assertValue(venues -> venues.size() == 1 && venues.get(0).equals(venue));
    }

    @Test
    public void givenTwoGroupsOfUnhandledType_whenGetVenuesByType_thenEmptyListOfVenuesIsEmitted() {
        setUpResponseWith2Groups();

        List<GroupItem> recommendedGroupItemList = new ArrayList<>();
        List<GroupItem> otherGroupList = new ArrayList<>();

        recommendedGroupItemList.add(recommendedGroupItem);
        otherGroupList.add(otherGroupItem);

        when(recommendedGroupItem.getVenue()).thenReturn(venue);

        when(recommendedGroup.getItems()).thenReturn(recommendedGroupItemList);
        when(recommendedGroup.getName()).thenReturn("otherUnexpectedGroupType");

        when(otherGroup.getItems()).thenReturn(otherGroupList);
        when(otherGroup.getName()).thenReturn("unexpectedGroupType");

        when(service.getRecommendedVenues(any())).thenReturn(Observable.just(venueRecommendations));

        serviceWrapper.getVenuesByType("anything", KnownGroupTypes.RECOMMENDED)
                .test()
                .assertNoErrors()
                .assertValue(List::isEmpty);
    }

    private void setUpResponseWith2Groups() {
        venueRecommendations = Mockito.mock(VenueRecommendations.class);
        venueRecommendationsResponse = Mockito.mock(VenueRecommendations.Response.class);
        recommendedGroup = Mockito.mock(Group.class);
        otherGroup = Mockito.mock(Group.class);
        venue = Mockito.mock(GroupItem.Venue.class);
        recommendedGroupItem = Mockito.mock(GroupItem.class);
        otherGroupItem = Mockito.mock(GroupItem.class);
        when(venueRecommendations.getResponse()).thenReturn(venueRecommendationsResponse);

        List<Group> groupList = new ArrayList<>();

        groupList.add(recommendedGroup);
        groupList.add(otherGroup);

        when(venueRecommendationsResponse.getGroups()).thenReturn(groupList);
    }
}