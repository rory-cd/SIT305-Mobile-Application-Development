# Task 2.1P - Travel Companion App

## Project Overview

A Travel Companion App, helping international travellers convert essential values - 
Currency, Fuel Efficiency, and Temperature. Conversion rates are based on fixed conversion factors, as provided below.

### Tech Stack
1. **Java** - Core logic
2. **XML (Material Design 3)** - UI layout
3. **Android View System** - UI paradigm
4. **Android Studio** - IDE

## Key Features

- Real-time conversion, removing the need for a standalone "convert" button
- Two-way conversion enables a more user-friendly experience
- Clear button, to perform fresh conversions more quickly
- Input validation avoids non-numeric inputs, negative values, etc.
- Input formatting makes large numbers more readable

## Implementation

- Data-agnostic UI allows UI elements to be re-used for each type of conversion
- Tabs as state, implementing a simplified version of the strategy pattern
- Boolean `isUpdating` flag prevents infinite loops from bi-directional data binding

## Conversion Factors

#### Currency Conversions
- 1 USD = 1.55 AUD
- 1 USD = 0.92 EUR
- 1 USD = 148.50 JPY
- 1 USD = 0.78 GBP

#### Fuel Efficiency
- 1 Mile per Gallon (mpg) = 0.425 Kilometers per Liter (km/L)
- 1 Gallon (US) = 3.785 Liters
- 1 Nautical Mile = 1.852 Kilometers

#### Temperature
- Celsius to Fahrenheit: 𝐹 = (𝐶 × 1.8) + 32
- Fahrenheit to Celsius: 𝐶 = (𝐹 − 32)/1.8
- Celsius to Kelvin: 𝐾 = 𝐶 + 273.15

## Installation

Open this folder in Android Studio and run on a suitable emulator:  
**Minimum OS:** API 24 (Nougat)  
**Target OS:** API 36 (Baklava)