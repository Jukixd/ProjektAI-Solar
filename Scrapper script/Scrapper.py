import requests
import pandas as pd
import time

MESTA = {
    "Praha": {"lat": 50.08, "lon": 14.43},
    "Brno": {"lat": 49.19, "lon": 16.60},
    "Ostrava": {"lat": 49.82, "lon": 18.26},
    "Plzen": {"lat": 49.73, "lon": 13.37},
    "Liberec": {"lat": 50.76, "lon": 15.05}
}

def scrapuj_pocasi():
    data = []

    for mesto, coords in MESTA.items():

        url = (
            f"https://archive-api.open-meteo.com/v1/archive?"
            f"latitude={coords['lat']}&longitude={coords['lon']}&"
            f"start_date=2025-01-01&end_date=2025-12-31&"
            f"hourly=temperature_2m,relative_humidity_2m,surface_pressure,cloud_cover,wind_speed_10m,shortwave_radiation"
        )
        try:
            response = requests.get(url)
            if response.status_code == 200:
                json_data = response.json()["hourly"]
                df_mesto = pd.DataFrame(json_data)
                df_mesto["mesto"] = mesto
                data.append(df_mesto)
            else:
                print(f"chyba u {mesto}: {response.status_code}")
            time.sleep(1)
        except Exception as e:
            print(f"chyba: {e}")

    finalni_df = pd.concat(data, ignore_index=True)
    finalni_df = finalni_df[finalni_df["shortwave_radiation"] > 0]
    finalni_df.to_csv("solarni_data_2025.csv", index=False)

if __name__ == "__main__":
    scrapuj_pocasi()