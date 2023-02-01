package part2;

import Part2.Country;
import Part2.Emission;
import Part2.Sector;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NOTE THAT THIS FILE WILL NOT COMPILE UNTIL YOU HAVE COPIED OVER YOUR
 * EMISSION, COUNTRY, AND SECTOR CLASSES TO THE part2 DIRECTORY
 */
public class PartTwoTestCases {
    @Test
    public void testYearWithHighestEmissions_Country(){
        //create test Object
        Map<Integer, Emission> emissions = new LinkedHashMap<>();
        emissions.put(1973,new Emission(3465.4, 5962.1, 4256.3));
        emissions.put(1975,new Emission(8642.1, 9293.6, 7641.6));
        emissions.put(1977,new Emission(4235.5, 1243.4, 8723.5));

        Country country = new Country("USA", emissions);
        assertEquals(1975, country.getYearWithHighestEmissions());
    }

    @Test
    public void testYearWithHighestEmissions_Sector(){
        //create test Object
        Map<Integer, Double> emissions = new LinkedHashMap<>();
        emissions.put(1970, 2278.8);
        emissions.put(1971, 2356.43);
        emissions.put(1972, 2243.3);
        Sector sector = new Sector("Transport", emissions);

        assertEquals(1971, sector.getYearWithHighestEmissions());
    }


    /**
     * Tests the implementation of the Emission class.
     *
     * TO-DO: Examine this test case to know what you should name your public methods.
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testEmissionImplSpecifics() throws NoSuchMethodException {
        final List<String> expectedMethodNames = Arrays.asList("getCO2", "getN2O", "getCH4");

        final List<Class> expectedMethodReturns = Arrays.asList(double.class, double.class, double.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0], new Class[0]);

        verifyImplSpecifics(Emission.class, expectedMethodNames, expectedMethodReturns,
                expectedMethodParameters);
    }

    /**
     * Tests the part2 implementation of the Country class.
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testCountryImplSpecifics() throws NoSuchMethodException {
        final List<String> expectedMethodNames = Arrays.asList("getName", "getEmissions", "getYearWithHighestEmissions");

        final List<Class> expectedMethodReturns = Arrays.asList(String.class, Map.class, int.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0], new Class[0]);

        verifyImplSpecifics(Country.class, expectedMethodNames, expectedMethodReturns,
                expectedMethodParameters);
    }

    /**
     * Tests the part2 implementation of the Sector class.
     *
     * @throws NoSuchMethodException
     */
    @Test
    public void testSectorImplSpecifics() throws NoSuchMethodException {
        final List<String> expectedMethodNames = Arrays.asList("getName", "getEmissions", "getYearWithHighestEmissions");

        final List<Class> expectedMethodReturns = Arrays.asList(String.class, Map.class, int.class);

        final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0], new Class[0]);

        verifyImplSpecifics(Sector.class, expectedMethodNames, expectedMethodReturns,
                expectedMethodParameters);
    }

    private static void verifyImplSpecifics(final Class<?> clazz, final List<String> expectedMethodNames,
                                            final List<Class> expectedMethodReturns, final List<Class[]> expectedMethodParameters)
            throws NoSuchMethodException {
        assertEquals(0, clazz.getFields().length, "Unexpected number of public fields");

        final List<Method> publicMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers())).collect(Collectors.toList());

        assertEquals(expectedMethodNames.size(), publicMethods.size(),
                "Unexpected number of public methods");

        assertTrue(expectedMethodNames.size() == expectedMethodReturns.size(),
                "Invalid test configuration");
        assertTrue(expectedMethodNames.size() == expectedMethodParameters.size(),
                "Invalid test configuration");

        for (int i = 0; i < expectedMethodNames.size(); i++) {
            Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i), expectedMethodParameters.get(i));
            assertEquals(expectedMethodReturns.get(i), method.getReturnType());
        }
    }

}
