import streamlit as st
from geopy.geocoders import Nominatim
from geopy.exc import GeocoderServiceError
from geopy.distance import geodesic


def get_distance(place1, place2):
    # Create a geocoder object
    geolocator = Nominatim(user_agent="my-app")

    try:
        # Geocode the place names to get their coordinates
        location1 = geolocator.geocode(place1)
        location2 = geolocator.geocode(place2)

        # Check if geocoding was successful
        if not location1 or not location2:
            st.write("Invalid place name. Please enter a valid place name.")
            return None

        # Calculate the distance between the two coordinates using geodesic distance
        distance = geodesic((location1.latitude, location1.longitude),
                            (location2.latitude, location2.longitude)).miles

        return distance

    except (AttributeError, GeocoderServiceError) as e:
        st.error(f"Error: {e}")
        return None


def walking(distance):
    emissions = distance * 404
    if emissions > 1000:
        emissions = round(emissions / 453.592, 2)
        unit = "pounds"
    else:
        unit = "grams"
    points = round(distance * 10)
    st.write(
        f"\nCongratulations! By walking {round(distance, 2)} miles with PlanetPath, you have saved {round(emissions, 2)} {unit} of CO2 emissions. Your small step has a huge impact on making our planet greener. Keep up the good work!")
    st.write(
        f"\nYou've also earned {points} points.")
    return points


def biking(distance):
    emissions = distance * 404
    if emissions > 1000:
        emissions = round(emissions / 453.592, 2)
        unit = "pounds"
    else:
        unit = "grams"
    points = round(distance * 10)
    st.write(
        f"\nCongratulations! By biking {round(distance, 2)} miles with PlanetPath, you have saved {round(emissions, 2)} {unit} of CO2 emissions. You're on the right track for making our planet greener. Keep up the good work!")
    st.write(
        f"\nYou've also earned {points} points.")
    return points


def bus(distance):
    emissions = distance * 209
    if emissions > 1000:
        emissions = round(emissions / 453.592, 2)
        unit = "pounds"
    else:
        unit = "grams"
    points = round(distance * 5)
    st.write(
        f"\nCongratulations! By using the bus for {round(distance, 2)} miles with PlanetPath, you have saved {round(emissions, 2)} {unit} of CO2 emissions. Your contribution matters in making our planet greener. Keep up the good work!")
    st.write(
        f"\nYou've also earned {points} points.")
    return points


def ride_share(distance):
    emissions = (distance * 404) - 404
    if emissions > 1000:
        emissions = round((emissions / 453.592)/2, 2)
        unit = "pounds"
    else:
        unit = "grams"
    points = round(distance * 3)
    st.write(
        f"\nCongratulations! By using ride share for {round(distance, 2)} miles with PlanetPath, you have saved {round(emissions, 2)} {unit} of CO2 emissions per passenger. Your efforts towards carpooling have made our planet greener. Keep up the good work!")
    st.write(
        f"\nYou've also earned {points} points.")
    return points


def driving(distance):
    emissions = distance * 404
    if emissions > 1000:
        emissions = round((emissions / 453.592)/2, 2)
        unit = "pounds"
    else:
        unit = "grams"
    st.write(
        f"\nBy driving alone, you would produce {round(emissions, 2)} {unit} of CO2 emissions for {round(distance, 2)} miles of travel. Consider using a more sustainable transportation method or carpooling to reduce your carbon footprint.")


def main():
    distance = 0
    st.set_page_config(
        page_title="PlanetPath",
        page_icon=":earth_africa:",
        layout="wide",
    )
    logo = 'https://green-transfer.fr/wp-content/uploads/2019/08/Green_Leaf_Earth_PNG_Clipart-2978.png'

    st.image(logo, width=300)
    st.title("PlanetPath")

    form = st.form(key='input-form')
    with form:
        # Prompt user to enter place names
        place1 = st.text_input("What is your starting point?")
        place2 = st.text_input("What is your destination?")

        # Prompt user to choose transportation option
        choice = st.selectbox("Choose a transportation option:", [
                              "Walking", "Biking", "Bus", "Ride Share", "Driving"])

        submit_button = st.form_submit_button(label='Calculate')

    # Get the distance between the two places after the user submits the form
    if submit_button:
        # Get the distance between the two places
        distance = get_distance(place1, place2)

        # Display CO2 emissions and points for the selected transportation option
        if choice == "Walking":
            walking(distance)
        elif choice == "Biking":
            biking(distance)
        elif choice == "Bus":
            bus(distance)
        elif choice == "Ride Share":
            ride_share(distance)
        elif choice == "Driving":
            driving(distance)
        else:
            st.write("Invalid choice. Please choose a transportation option.")

    if st.button("Home Page"):
        st.experimental_rerun()


if (__name__ == "__main__"):
    main()
