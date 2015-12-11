package com.lach.translink.ui.impl.resolve;

import com.lach.translink.data.location.PlaceType;
import com.lach.translink.data.place.bus.BusStop;
import com.lach.common.data.map.MapPosition;

public interface ResolveLocationEvents {

    class ShowMapEvent {
    }

    class MapAddressSelectedEvent {
        private final MapPosition point;

        public MapAddressSelectedEvent(MapPosition point) {
            this.point = point;
        }

        public MapPosition getPoint() {
            return point;
        }
    }

    class MapBusStopSelectedEvent {
        private final BusStop busStop;

        public MapBusStopSelectedEvent(BusStop busStop) {
            this.busStop = busStop;
        }

        public BusStop getBusStop() {
            return busStop;
        }
    }

    class LocationSelectedEvent {
        private final PlaceType placeType;
        private final String address;
        private final boolean favourite;

        public LocationSelectedEvent(PlaceType placeType, String address, boolean favourite) {
            this.placeType = placeType;
            this.address = address;
            this.favourite = favourite;
        }

        public PlaceType getPlaceType() {
            return placeType;
        }

        public String getAddress() {
            return address;
        }

        public boolean isFavourite() {
            return favourite;
        }
    }
}