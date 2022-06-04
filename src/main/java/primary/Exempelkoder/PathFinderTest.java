//package prog2vt22.i2;
//
//import javafx.collections.ObservableList;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.control.*;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.Shape;
//import javafx.scene.text.Text;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.stage.Window;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.testfx.api.FxRobot;
//import org.testfx.api.FxToolkit;
//import org.testfx.framework.junit5.ApplicationExtension;
//import org.testfx.util.WaitForAsyncUtils;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assumptions.assumeTrue;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(ApplicationExtension.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class PathFinderTest {
//	//
//	private static final String UPDATED_DATE = "2022-05-23 11:00";
//
//	// Files etc.
//	private static final String IMAGE_EUROPA_FILE_NAME = "file:europa.gif";
//	private static final int IMAGE_EUROPA_WIDTH = 618;
//	private static final int IMAGE_EUROPA_HEIGHT = 729;
//	private static final String ALTERNATE_IMAGE_PATH = "file:karta.png";
//	private static final int ALTERNATE_IMAGE_WIDTH = 960;
//	private static final int ALTERNATE_IMMAGE_HEIGHT = 720;
//	private static final String FILE_EUROPA_GRAPH = "europa.graph";
//	private static final int EUROPA_GRAPH_NODE_COUNT = 10;
//	private static final int EUROPA_GRAPH_EDGE_COUNT = 18;
//	private static final String FILE_CAPTURE_PNG = "capture.png";
//
//	// Menus
//	private static final String MENU = "#menu";
//	private static final String MENU_FILE = "#menuFile";
//	private static final String MENU_NEW_MAP = "#menuNewMap";
//	private static final String MENU_OPEN_FILE = "#menuOpenFile";
//	private static final String MENU_SAVE_FILE = "#menuSaveFile";
//	private static final String MENU_SAVE_IMAGE = "#menuSaveImage";
//	private static final String MENU_EXIT = "#menuExit";
//
//	// Buttons
//	private static final String BTN_NEW_CONNECTION = "#btnNewConnection";
//	private static final String BTN_CHANGE_CONNECTION = "#btnChangeConnection";
//	private static final String BTN_NEW_PLACE = "#btnNewPlace";
//	private static final String BTN_SHOW_CONNECTION = "#btnShowConnection";
//	private static final String BTN_FIND_PATH = "#btnFindPath";
//
//	// Other stuff
//	private static final String APP_TITLE = "PathFinder";
//	private static final String MAP = "#outputArea";
//	private static final String TESTING_ABORTED = "Fel: testningen avbröts eftersom föregående steg inte lyckades.";
//	//
//	private Step lastStep = Step.INIT;
//	//
//	private Stage stage;
//	private Graph<Object> graph;
//	private FxRobot robot;
//	private Pane center;
//	private int minX, minY;
//	private PathFinder app;
//	private final StringBuilder state = new StringBuilder();
//
//	@BeforeAll
//	static void _beforeAll() {
//
//		Properties properties = System.getProperties();
//		if (!properties.get("os.name").equals("Mac OS X")) {
//			System.setProperty("prism.order", "sw");
//		}
////		if (properties.get("os.name").equals("Mac OS X")) {
////			System.setProperty("java.awt.headless", "false");
////			System.setProperty("testfx.headless", "false");
////		} else {
//		System.setProperty("java.awt.headless", "true");
//		System.setProperty("testfx.headless", "true");
////		}
//
//		System.setProperty("testfx.robot", "glass");
//		System.setProperty("prism.text", "t2k");
//		System.setProperty("headless.geometry", "1600x1200-32");
////		System.setProperty("headless.geometry", "1280x800-32");
//	}
//
//	private static List<Node> _helper_get_child_nodes_from(Parent root) {
//		var nodes = new ArrayList<Node>();
//		_helper_get_all_descendents(root, nodes);
//		return nodes;
//	}
//
//	private static void _helper_get_all_descendents(Parent parent, List<Node> nodes) {
//		for (Node node : parent.getChildrenUnmodifiable()) {
//			nodes.add(node);
//			if (node instanceof Parent)
//				_helper_get_all_descendents((Parent) node, nodes);
//		}
//	}
//
//	@Test
//	@Order(0)
//	@DisplayName("Information")
//	void __version() {
//		System.out.printf("Test uppdaterat %s%n", UPDATED_DATE);
//	}
//
//	private void _helper_add_edge(String name, int weight) {
//		robot.clickOn(BTN_NEW_CONNECTION);
//		_helper_fill_dialog_new_connection(name, weight);
//	}
//
//	private void _helper_add_edge(Node node1, Node node2, String name, int weight) {
//		_helper_add_edge(name, weight);
//		if (graph.getEdgeBetween(node1, node2) == null)
//			fail("Fel: getEdgeBetween returnerar null efter att New Connection har gjorts.");
//	}
//
//	private void _helper_add_n_places(int n) {
//
//		assertNotNull(center, "Fel: objektet som ska peka på outputArea är null.");
//
//		var childCountBefore = center.getChildren().size();
//		var nodesBefore = _helper_get_node_count_from_graph();
//		for (int i = 1; i <= n; i++) {
//			_helper_add_place("Node" + i, 50 * i, 50 * i);
//			assertTrue(childCountBefore < center.getChildren().size(), "Fel: kunde inte skapa nod. Kontrollera att saker händer i rätt ordning vid klick på karta.");
//			var node = robot.lookup(String.format("#Node%d", i)).tryQuery();
//			if (node.isEmpty()) fail("Fel: kunde inte hitta nyskapad nod. Kolla att den har id satt till sitt namn.");
//		}
//
//		var childCountAfter = center.getChildren().size();
//		assertTrue(childCountAfter >= childCountBefore + n, "Fel: antalet objekt på kartan är färre än förväntat.");
//
//		if (graph.getNodes().isEmpty())
//			fail("Fel: grafen borde inte vara tom efter att platser har lagts till med New Place.");
//
//		assertEquals(n + nodesBefore, _helper_get_node_count_from_graph());
//	}
//
//	private void _helper_add_place(String name, int x, int y) {
//		robot.clickOn(BTN_NEW_PLACE);
//		robot.clickOn(minX + x, minY + y);
//		_helper_fill_dialog_new_place(name);
//	}
//
//	private void _helper_check_if_click_worked(Node node, boolean clicked) {
//		Paint fill = Color.BLACK;
//
//		if (node instanceof Shape node1) {
//			fill = node1.getFill();
//		} else if (node instanceof Canvas node1) {
//			fill = node1.getGraphicsContext2D().getFill();
//		} else if (node instanceof Group group) {
//			for (Node child : group.getChildren()) {
//				if (child instanceof Shape shape && !(child instanceof Text))
//					fill = shape.getFill();
//			}
//		} else {
//			fail("Kunde inte analysera vilken subklass till Node nodklassen är.");
//		}
//
//		if (clicked)
//			assertEquals(Color.RED, fill, "Fel: förväntade att klickad omarkerad nod skulle bli markerad och få färgen Color.RED.");
//		else
//			assertEquals(Color.BLUE, fill, "Fel: förväntade att klickad markerad nod skulle bli avmarkerad och få färgen Color.BLUE. BLUE.");
//	}
//
//	private void _helper_check_if_node_is_not_selected(Node node) {
//		_helper_check_if_click_worked(node, false);
//	}
//
//	private void _helper_check_if_node_is_selected(Node node) {
//		_helper_check_if_click_worked(node, true);
//	}
//
//	private void _helper_click_cancel_if_dialog_blocks() {
//		if (_helper_get_top_modal_stage() != null) robot.clickOn("Cancel");
//	}
//
//	private void _helper_click_ok_if_dialog_blocks() {
//		if (_helper_get_top_modal_stage() != null) robot.clickOn("OK");
//	}
//
//	private void _helper_click_on_menu(String menu) {
//		robot.clickOn(MENU_FILE);
//		robot.clickOn(menu);
//	}
//
//	private void _helper_deselect(Node node) {
//		robot.clickOn(node);
//		_helper_check_if_click_worked(node, false);
//	}
//
//	private void _helper_fill_dialog_change_connection(final String time) {
//		final var actualAlertDialog = _helper_get_top_modal_stage();
//		assertNotNull(actualAlertDialog);
//
//		final var dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
//
//		var children = _helper_get_child_nodes_from(dialogPane);
//
//		TextField nameField = null, timeField = null;
//		for (Node child : children) {
//			if (child instanceof TextField field) {
//				if (nameField == null) {
//					nameField = field;
//				} else {
//					timeField = field;
//					break;
//				}
//			}
//		}
//
//		if (nameField == null || timeField == null)
//			fail("Kunde inte hitta textfälten i dialogrutan för ändra förbindelse.");
//
//		timeField.setText(String.valueOf(time));
//
//		robot.clickOn("OK");
//	}
//
//	private void _helper_fill_dialog_new_connection(final String name, final int time) {
//		final var actualAlertDialog = _helper_get_top_modal_stage();
//		assertNotNull(actualAlertDialog);
//
//		final var dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
//
//		var children = _helper_get_child_nodes_from(dialogPane);
//
//		TextField nameField = null, timeField = null;
//		for (Node child : children) {
//			if (child instanceof TextField && nameField == null)
//				nameField = (TextField) child;
//			else if (child instanceof TextField) {
//				timeField = (TextField) child;
//				break;
//			}
//		}
//
//		if (nameField == null || timeField == null)
//			fail("Kunde inte hitta textfälten i dialogrutan för ny förbindelse.");
//
//		nameField.setText(name);
//		timeField.setText(String.valueOf(time));
//
//		robot.clickOn("OK");
//	}
//
//	private void _helper_fill_dialog_new_place(final String name) {
//
//		final var actualAlertDialog = _helper_get_top_modal_stage();
//
//		if (actualAlertDialog == null)
//			fail("Fel: ingen dialogruta för inmatning av namn för ny plats verkar ha öppnats.");
//
//		final var dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
//
//		var children = _helper_get_child_nodes_from(dialogPane);
//
//		children
//				.stream()
//				.filter(TextField.class::isInstance)
//				.findFirst()
//				.map(TextField.class::cast)
//				.ifPresentOrElse(field -> {
//							field.setText(name);
//							robot.clickOn("OK");
//						},
//						() -> fail("Kunde inte hitta textfältet i dialogrutan för ny plats.")
//				);
//	}
//
//	private int _helper_get_edge_count_from_graph() {
//		return graph.getNodes()
//				.stream()
//				.map(graph::getEdgesFrom)
//				.map(Collection::size)
//				.reduce(0, Integer::sum);
//	}
//
//	@SuppressWarnings("unchecked")
//	private Graph<Object> _helper_get_graph_with_reflection() throws IllegalAccessException {
//		Field graph = null;
//
//		int graphCount = 0;
//		for (Field declaredField : PathFinder.class.getDeclaredFields()) {
//			if (declaredField.getType().isAssignableFrom(ListGraph.class)) {
//				graphCount++;
//				graph = declaredField;
//			}
//		}
//
//		if (graph == null)
//			fail("Fel: kunde inte hitta medlemsvariabeln för grafen (ListGraph).");
//
//		if (graphCount > 1)
//			fail("Fel: hittade mer än en medlemsvariabel av typen ListGraph.");
//
//		graph.setAccessible(true);
//		return (Graph<Object>) graph.get(app);
//	}
//
//	private int _helper_get_node_count_from_graph() {
//		return graph.getNodes().size();
//	}
//
//	private Stage _helper_get_top_modal_stage() {
//		final List<Window> allWindows = new ArrayList<>(robot.robotContext().getWindowFinder().listWindows());
//		Collections.reverse(allWindows);
//
//		return allWindows
//				.stream()
//				.filter(Stage.class::isInstance)
//				.map(Stage.class::cast)
//				.filter(window -> window.getModality() == Modality.APPLICATION_MODAL)
//				.findFirst()
//				.orElse(null);
//	}
//
//	private void _helper_init_after_map_is_loaded() throws IllegalAccessException {
//		this.center = robot.lookup(MAP).query();
//		var bounds = center.localToScreen(center.getBoundsInLocal());
//		this.minX = (int) bounds.getMinX();
//		this.minY = (int) bounds.getMinY();
//		_helper_reload_graph();
//	}
//
//	private void _helper_open_new_map() throws IllegalAccessException {
//		_helper_click_on_menu(MENU_NEW_MAP);
//		_helper_init_after_map_is_loaded();
//	}
//
//	private void _helper_reload_graph() throws IllegalAccessException {
//		this.graph = _helper_get_graph_with_reflection();
//	}
//
//	private void _helper_reload_graph_or_fail(int expectedNodeCount) throws IllegalAccessException {
//		// TODO: synkroniseringsproblem... ta reda på orsak och fixa bättre lösning.
//		_helper_reload_graph();
//		if (graph.getNodes().size() != expectedNodeCount)
//			_helper_reload_graph();
//		if (graph.getNodes().size() != expectedNodeCount)
//			fail("Fel: grafen har fel antal noder efter New Map eller Open. Det borde ha varit " + expectedNodeCount + " men var " + graph.getNodes().size());
//	}
//
//	private void _helper_restore_alternate_graph_file() throws IOException {
//		Files.copy(Path.of(FILE_EUROPA_GRAPH + ".sav"), Path.of(FILE_EUROPA_GRAPH), StandardCopyOption.REPLACE_EXISTING);
//	}
//
//	private void _helper_restore_original_graph_file() throws IOException {
//		Files.copy(Path.of(FILE_EUROPA_GRAPH + ".org"), Path.of(FILE_EUROPA_GRAPH), StandardCopyOption.REPLACE_EXISTING);
//	}
//
//	private void _helper_select(Node node) {
//		robot.clickOn(node);
//		_helper_check_if_click_worked(node, true);
//	}
//
//	private void _helper_validate_alert(final String expectedHeader, final String expectedContent) {
//		final Stage actualAlertDialog = _helper_get_top_modal_stage();
//		assertNotNull(actualAlertDialog, "Fel: en dialogruta/alert med ett felmeddelande borde ha visats, men det verkar inte ha skett.");
//
//		// TODO: utkommentarat pga för mycket variation
//		//		final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
//		//		String headerOrTitleText = dialogPane. !=null?dialogPane.getHeaderText():actualAlertDialog.getTitle();
//		//		assertTrue(headerOrTitleText.contains(expectedHeader));
//		//		assertEquals(expectedContent, dialogPane.getContentText());
//		robot.clickOn("OK");
//	}
//
//	private void _helper_validate_saved_file() throws IOException {
//
//		assertTrue(Files.exists(Path.of(FILE_EUROPA_GRAPH)), "Fel: filen europa.graph finns inte. Kontrollera att filen sparas till rätt sökväg.");
//
//		var actualLines = Files.readAllLines(Path.of(FILE_EUROPA_GRAPH));
//
//		var nodeTokens = Set.of("Node1", "Node2", "50.0", "100.0");
//
//		var nodeLine = actualLines.get(1);
//
//		if (nodeLine.contains(","))
//			fail("Fel: decimaltal har sparats med komma som decimalseparator istället för punkt som uppgiften kräver, vilket gör att filen inte kan läsas in.");
//
//		for (String nodeToken : nodeTokens) {
//			if (!nodeLine.contains(nodeToken))
//				fail("Fel: den sparade filen innehåller inte all, eller rätt, information. \n" +
//						"Hittade inte '" + nodeToken + "'.\n" +
//						"Följande rader hittades: " + nodeLine);
//		}
//
//		var expectedLines = List.of(
//				"file:europa.gif",
//				"Node1;Node2;Node1->Node2;5",
//				"Node2;Node1;Node1->Node2;5"
//		);
//
//		for (String expectedLine : expectedLines) {
//			if (!actualLines.contains(expectedLine))
//				fail("Fel: den sparade filen innehåller inte all, eller rätt, information. \n" +
//						"Hittade inte '" + expectedLine + "'.\n" +
//						"Följande rader hittades: " + actualLines);
//		}
//	}
//
//	private void _helper_validate_unsaved_changes_warning_and_cancel() {
//		final Stage alertDialog = _helper_get_top_modal_stage();
//		assertNotNull(alertDialog, "Fel: ingen dialogruta med varning om osparade ändringar verkar visas vid Open efter att New Map har gjorts tidigare; det räknas som en osparad ändring.");
//		final DialogPane dialogPane = (DialogPane) alertDialog.getScene().getRoot();
//
//		ObservableList<ButtonType> buttonTypes = ((DialogPane) alertDialog.getScene().getRoot()).getButtonTypes();
//		String localizedCancelText = buttonTypes.stream().filter(buttonType -> buttonType == ButtonType.CANCEL).findFirst().get().getText();
//
//		// TODO: kanske borde kolla andra ord, eller inget alls...
//		if (dialogPane != null && dialogPane.getContentText().contains("Unsaved"))
//			robot.clickOn(localizedCancelText);
//	}
//
//	private void _helper_verify_correct_image(String expectedFilename, int expectedWidth, int expectedHeight) {
//		this.center = robot.lookup(MAP).query();
//		var children = _helper_get_child_nodes_from(center);
//
//		var countOfImageView = children.stream()
//				.filter(ImageView.class::isInstance)
//				.map(ImageView.class::cast)
//				.count();
//
//		if (countOfImageView > 1)
//			fail("Fel: det borde inte finnas mer än ett enda ImageView-objekt bland barnen till komponenten med id 'outputArea'.");
//
//		var imageView = children.stream()
//				.filter(ImageView.class::isInstance)
//				.map(ImageView.class::cast)
//				.findFirst();
//
//		imageView.ifPresentOrElse(node -> {
//
//			var actualFilename = node.getImage().getUrl();
//			if (actualFilename.contains("/"))
//				actualFilename = "file:" + actualFilename.substring(actualFilename.lastIndexOf('/') + 1);
//
//			var actualHeight = node.getImage().getHeight();
//			var actualWidth = node.getImage().getWidth();
//			var error = node.getImage().isError();
//
//			assertFalse(error, "Fel: kunde inte ladda in bilden. Kolla att sökvägen är korrekt.");
//			assertEquals(expectedFilename, actualFilename, "Fel: bilden har fel url. Det borde ha varit '" + expectedFilename + "' som finns på första raden i filen, men det var '" + actualFilename + "'.");
//			assertEquals(expectedWidth, actualWidth, "Fel: bilden har inte rätt bredd. Borde vara " + expectedWidth + " men var " + actualWidth);
//			assertEquals(expectedHeight, actualHeight, "Fel: bilden har inte rätt höjd. Borde vara " + expectedHeight + " men var " + actualHeight);
//		}, () -> fail("Fel: kunde inte hitta ImageView-komponenten"));
//	}
//
//	private void _test_ending(Step step) {
//		state.append(String.format("%n>>> Steg %s slutar. <<< %n", step));
//		System.err.printf("%n>>> Steg %s slutar. <<< %n", step);
//		lastStep = step;
//	}
//
//	private void _test_starting(Step step) {
//		var last = Step.values()[step.ordinal() - 1];
//		assumeTrue(lastStep == last, TESTING_ABORTED);
//		state.append(String.format("%n>>> Steg %s startar. <<< %n", step));
//		System.err.printf("%n>>> Steg %s startar. <<< %n", step);
//	}
//
//	@BeforeAll
//	public void setupClass(FxRobot robot) throws Exception {
//
//		this.stage = FxToolkit.registerPrimaryStage();
//
//		this.app = (PathFinder) FxToolkit.setupApplication(PathFinder.class);
//		assertNotNull(this.app, "Fel: kunde inte starta applikationen.");
//
//		_helper_reload_graph();
//		assertNotNull(this.graph, "Fel: kunde inte hitta medlemsvariabeln för grafen. Den bör vara deklarerad som\n" +
//				"Graph<DIN_NODTYP> graph = new ListGraph<>();");
//
//		this.robot = robot;
//		assertNotNull(this.robot, "Fel: internt fel i testramverket.");
//	}
//
//	private void sleep(int sec) {
//		WaitForAsyncUtils.sleep(sec, TimeUnit.SECONDS);
//	}
//
//	@AfterAll
//	void tearDown() throws TimeoutException {
//		FxToolkit.cleanupStages();
//	}
//
//	@Test
//	@DisplayName("Verifiera att kontroller har id satta med setId.")
//	@Order(0)
//	public void test00_hasCorrectlyNamedControls() {
//
//		_test_starting(Step.IDS);
//
//		var menuControls = Map.of(
//				MENU_NEW_MAP, "menyvalet New Map"
//				, MENU_OPEN_FILE, "menyvalet Open"
//				, MENU_SAVE_FILE, "menyvalet Save"
//				, MENU_SAVE_IMAGE, "menyvalet Save Image"
//				, MENU_EXIT, "menyvalet Exit"
//		);
//
//		var menu = robot.lookup(MENU).tryQuery();
//		var fileMenuIds = new HashSet<>();
//		menu.ifPresentOrElse(node -> ((MenuBar) node).getMenus().get(0).getItems().stream().map(MenuItem::getId).forEach(fileMenuIds::add)
//				, () -> fail("Kunde inte hitta menuBar med id 'menu'")
//		);
//
//		menuControls.forEach((k, v) -> assertTrue(fileMenuIds.contains(k.substring(1)),
//				String.format("Kontrollen för %s kunde inte hittas. Den ska ha id '%s' satt med setId.", v, k.substring(1)))
//		);
//
//		var otherControls = Map.of(
//				BTN_FIND_PATH, "Knappen Find Path",
//				BTN_SHOW_CONNECTION, "Knappen Show Connection",
//				BTN_NEW_PLACE, "Knappen New Place",
//				BTN_CHANGE_CONNECTION, "Knappen Change Connection",
//				BTN_NEW_CONNECTION, "Knappen New Connection",
//				MAP, "Den komponent som platser adderas till (en Pane)"
//		);
//
//		for (var entry : otherControls.entrySet()) {
//			var node = robot.lookup(entry.getKey()).tryQuery();
//			if (node.isEmpty())
//				fail(String.format("%s kunde inte hittas. %nDen ska ha id '%s' satt med setId och måste finnas tillagd från start (dvs. efter att metoden start har kört klart).", entry.getValue(), entry.getKey().substring(1)));
//		}
//
//		var outputArea = robot.lookup(MAP).query();
//		if (!(outputArea instanceof Pane)) {
//			fail("Fel: id outputArea ska vara kopplat till den Pane som platser läggs till på.");
//		}
//
//		_test_ending(Step.IDS);
//	}
//
//	@Test
//	@DisplayName("Verifiera att applikationen har rätt titel.")
//	@Order(10)
//	void test01_check_title() {
//
//		_test_starting(Step.TITLE);
//
//		assertEquals(APP_TITLE, stage.getTitle(), "Titel på stage borde ha varit '%s'.".formatted(APP_TITLE));
//
//		_test_ending(Step.TITLE);
//	}
//
//	@Test
//	@DisplayName("Testar New Map.")
//	@Order(20)
//	void test02_new_map() throws IllegalAccessException {
//
//		_test_starting(Step.NEW_MAP);
//
//		_helper_open_new_map();
//
//		_helper_verify_correct_image(IMAGE_EUROPA_FILE_NAME, IMAGE_EUROPA_WIDTH, IMAGE_EUROPA_HEIGHT);
//
//		_test_ending(Step.NEW_MAP);
//	}
//
//	@Test
//	@DisplayName("Testar New Place.")
//	@Order(30)
//	void test03_new_place() {
//
//		_test_starting(Step.NEW_PLACE);
//
//		_helper_add_n_places(4);
//
//		var node = _helper_get_node_by_id("#Node1");
//
//		_helper_select(node);
//
//		_helper_deselect(node);
//
//		_test_ending(Step.NEW_PLACE);
//	}
//
//	private Node _helper_get_node_by_id(String id) {
//		return robot.lookup(id).query();
//	}
//
//	@Test
//	@DisplayName("Testar New Connection.")
//	@Order(40)
//	void test04_new_connection() {
//
//		_test_starting(Step.NEW_CONN);
//
//		var node1 = _helper_get_node_by_id("#Node1");
//		var node2 = _helper_get_node_by_id("#Node2");
//		var node3 = _helper_get_node_by_id("#Node3");
//
//		assertFalse(graph.pathExists(node1, node3), "Fel: det borde inte finnas någon väg mellan nod1 och nod3 innan bågar har skapats.");
//
//		_helper_select(node1);
//		_helper_select(node2);
//
//		_helper_add_edge(node1, node2, "Node1->Node2", 5);
//
//		robot.clickOn(BTN_NEW_CONNECTION);
//		_helper_validate_alert("Error", "Connection already exists!");
//
//		_helper_deselect(node1);
//		_helper_deselect(node2);
//		_helper_select(node3);
//
//		robot.clickOn(BTN_NEW_CONNECTION);
//		_helper_validate_alert(null, "Two places must be selected!");
//
//		_helper_select(node2);
//
//		_helper_add_edge(node2, node3, "Node2->Node3", 15);
//		if (!graph.pathExists(node1, node3))
//			fail("Fel: det borde ha funnits en väg mellan noderna 1 och 3.");
//
//		_test_ending(Step.NEW_CONN);
//	}
//
//	@Test
//	@DisplayName("Testar Open.")
//	@Order(50)
//	void test05_open() throws IllegalAccessException, IOException {
//
//		_test_starting(Step.OPEN);
//
//		_helper_restore_alternate_graph_file();
//
//		_helper_click_on_menu(MENU_OPEN_FILE);
//		if (_helper_get_top_modal_stage() == null)
//			fail("Fel: ingen dialogruta med varning om osparade ändringar verkar visas vid Open efter att New Map har gjorts tidigare; det räknas som en osparad ändring.");
//		robot.clickOn("OK");
//
//		_helper_verify_correct_image(ALTERNATE_IMAGE_PATH, ALTERNATE_IMAGE_WIDTH, ALTERNATE_IMMAGE_HEIGHT);
//
//		_helper_restore_original_graph_file();
//
//		_helper_click_on_menu(MENU_OPEN_FILE);
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_verify_correct_image(IMAGE_EUROPA_FILE_NAME, IMAGE_EUROPA_WIDTH, IMAGE_EUROPA_HEIGHT);
//
//		_helper_reload_graph_or_fail(EUROPA_GRAPH_NODE_COUNT);
//
//		var edgeCount = _helper_get_edge_count_from_graph();
//		assertEquals(EUROPA_GRAPH_EDGE_COUNT, edgeCount, "Fel: grafen har fel antal kanter efter att ha laddat in testfilen europa.graph.");
//
//		var oslo = robot.lookup("#Oslo").tryQuery();
//		if (oslo.isEmpty())
//			fail("Fel: kunde inte hitta noden med id Oslo efter att ha laddat in europa.graph. Kolla att nodens id är satt till dess namn.");
//		var node1 = oslo.get();
//		var node2 = _helper_get_node_by_id("#Berlin");
//
//		var msg = "Kontrollerar att %s för %s är %d efter att europa.graph har laddats in.";
//		var x1 = node1.getLayoutX() == 0.0 ? node1.getBoundsInLocal().getCenterX() : node1.getLayoutX();
//		var y1 = node1.getLayoutY() == 0.0 ? node1.getBoundsInLocal().getCenterY() : node1.getLayoutY();
//		assertEquals(398.0, x1, String.format(msg, "x", "Oslo", 398));
//		assertEquals(220.0, y1, String.format(msg, "y", "Oslo", 220));
//
//		var x2 = (node2.getLayoutX() == 0.0) ? node2.getBoundsInLocal().getCenterX() : node2.getLayoutX();
//		var y2 = (node2.getLayoutY() == 0.0) ? node2.getBoundsInLocal().getCenterY() : node2.getLayoutY();
//		assertEquals(411.0, x2, String.format(msg, "x", "Berlin", 411));
//		assertEquals(368.0, y2, String.format(msg, "y", "Berlin", 369));
//
//		assertTrue(graph.pathExists(node1, node2), "Fel: det borde ha funnits en path mellan Oslo och Berlin efter att europa.graph had laddats in.");
//
//		_helper_select(node1);
//		_helper_select(node2);
//
//		_test_ending(Step.OPEN);
//	}
//
//	@Test
//	@DisplayName("Testar Save.")
//	@Order(60)
//	void test06_save() throws IllegalAccessException, IOException {
//
//		_test_starting(Step.SAVE);
//
//		_helper_open_new_map();
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_reload_graph_or_fail(0);
//
//		_helper_add_n_places(2);
//
//		var node1 = _helper_get_node_by_id("#Node1");
//		_helper_select(node1);
//
//		var node2 = _helper_get_node_by_id("#Node2");
//		_helper_select(node2);
//
//		_helper_add_edge(node1, node2, "Node1->Node2", 5);
//
//		_helper_click_on_menu(MENU_SAVE_FILE);
//		_helper_validate_saved_file();
//
//		_helper_open_new_map();
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_reload_graph_or_fail(0);
//		if(!graph.getNodes().isEmpty())
//			fail("Fel: en ny karta har laddats in med New map, men grafen är inte tom.");
//
//		_helper_click_on_menu(MENU_OPEN_FILE);
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_reload_graph_or_fail(2);
//
//		if (_helper_get_node_count_from_graph() != 2)
//			fail("Fel: grafen har fel antal noder efter att den sparade filen har lästs in. Det skulle vara 2, men var " + graph.getNodes().size());
//
//		if (_helper_get_edge_count_from_graph() != 2)
//			fail("Fel: grafen har fel antal bågar efter att den sparade filen har lästs in. Det skulle vara 2, men var " + _helper_get_edge_count_from_graph());
//
//		var n1 = _helper_get_node_by_id("#Node1");
//		var n2 = _helper_get_node_by_id("#Node2");
//
//		if (graph.getEdgeBetween(n1, n2) == null)
//			fail("Fel: det borde ha funnits en båge efter att den sparade textfilen lästs in.");
//
//		_helper_select(n1);
//		_helper_select(n2);
//
//		_helper_restore_original_graph_file();
//
//		_helper_click_on_menu(MENU_OPEN_FILE);
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_reload_graph_or_fail(EUROPA_GRAPH_NODE_COUNT);
//
//		if (_helper_get_node_count_from_graph() != EUROPA_GRAPH_NODE_COUNT)
//			fail("Fel: grafen har fel antal noder efter att den sparade filen har lästs in. Det skulle vara " + EUROPA_GRAPH_NODE_COUNT + ", men var " + graph.getNodes().size());
//
//		_test_ending(Step.SAVE);
//	}
//
//	@Test
//	@DisplayName("Testar Save Image.")
//	@Order(70)
//	void test07_save_image() throws IOException {
//
//		_test_starting(Step.SAVE_IMG);
//
//		assertFalse(Files.exists(Path.of(FILE_CAPTURE_PNG)));
//
//		_helper_click_on_menu(MENU_SAVE_IMAGE);
//
//		assertTrue(Files.exists(Path.of(FILE_CAPTURE_PNG)));
//
//		Files.delete(Path.of(FILE_CAPTURE_PNG));
//
//		_test_ending(Step.SAVE_IMG);
//	}
//
//	@Test
//	@DisplayName("Testar Find Path.")
//	@Order(80)
//	void test08_find_path() {
//
//		_test_starting(Step.FIND_PATH);
//
//		var node1 = _helper_get_node_by_id("#Madrid");
//		var node2 = _helper_get_node_by_id("#London");
//
//		if (!graph.pathExists(node1, node2))
//			fail("Fel: enligt grafen finns det ingen väg mellan London och Madrid vilket det ska göra.");
//
//		_helper_select(node1);
//		_helper_select(node2);
//
//		robot.clickOn(BTN_FIND_PATH);
//
//		// TODO: borde inte hårdkoda till TextArea kanske
//		var dialog = _helper_get_top_modal_stage();
//		var children = _helper_get_child_nodes_from(dialog.getScene().getRoot());
//		var textArea = children.stream().filter(TextArea.class::isInstance).map(TextArea.class::cast).findFirst();
//
//		textArea.ifPresentOrElse(
//				node -> {
//					var text = node.getText();
//
//					if (!text.contains("\n"))
//						fail("Fel: vägen ska skrivas ut på flera rader, inte som en lång sträng på samma rad.");
//
//					var lines = text.split("\n");
//
//					var expectedLines = Set.of("to Paris by Train takes 10", "to London by Train takes 2", "Total 12");
//					var actualLines = Arrays.stream(lines).map(String::trim).map(s -> s.replace(":", "")).collect(Collectors.toSet());
//					assertTrue(actualLines.containsAll(expectedLines), "Textrutan med vägen innehöll inte rätt information.\nSökte efter:\n" + expectedLines + "\nmen hittade:\n" + actualLines);
//					robot.clickOn("OK");
//				},
//				() -> fail("Kunde inte hitta textarean med vägen.")
//		);
//
//		_test_ending(Step.FIND_PATH);
//	}
//
//	@Test
//	@DisplayName("Testar Show Connection.")
//	@Order(90)
//	void test09_show_connection() throws TimeoutException, IllegalAccessException {
//
//		_test_starting(Step.SHOW_CONN);
//
//		var node1 = _helper_get_node_by_id("#London");
//		var node2 = _helper_get_node_by_id("#Dublin");
//		var node3 = _helper_get_node_by_id("#Madrid");
//
//		_helper_check_if_node_is_selected(node1);
//		_helper_deselect(node3);
//		_helper_select(node2);
//
//		robot.clickOn(BTN_SHOW_CONNECTION);
//
//		var dialog = _helper_get_top_modal_stage();
//		var children = _helper_get_child_nodes_from(dialog.getScene().getRoot());
//		var actual = children
//				.stream()
//				.filter(TextField.class::isInstance)
//				.map(TextField.class::cast)
//				.map(TextField::getText)
//				.collect(Collectors.toSet());
//
//		var expected = List.of("Boat", "15");
//
//		for (String expectedLine : expected) {
//			if (!actual.contains(expectedLine))
//				fail("Fel: dialogrutan för Show Connection innehåller inte all, eller rätt, information. \n" +
//						"Hittade inte '" + expectedLine + "'.\n" +
//						"Följande rader hittades: " + actual);
//		}
//
//		robot.clickOn("OK");
//
//		_test_ending(Step.SHOW_CONN);
//	}
//
//	@Test
//	@DisplayName("Testar Change Connection.")
//	@Order(100)
//	void test10_change_connection() throws TimeoutException, IllegalAccessException {
//
//		_test_starting(Step.CHG_CONN);
//
//		var node1 = _helper_get_node_by_id("#London");
//		var node2 = _helper_get_node_by_id("#Dublin");
//
//		_helper_check_if_node_is_selected(node1);
//		_helper_check_if_node_is_selected(node2);
//
//		robot.clickOn(BTN_CHANGE_CONNECTION);
//		_helper_fill_dialog_change_connection("10");
//
//		var weight = graph.getEdgeBetween(node1, node2).getWeight();
//		assertEquals(10, weight, "Fel: bågen har fel vikt efter Change Connection skulle ha satt vikten till 10.");
//
//		_test_ending(Step.CHG_CONN);
//	}
//
//	@Test
//	@DisplayName("Testar Exit.")
//	@Order(110)
//	void test11_exit() throws TimeoutException, IllegalAccessException {
//
//		_test_starting(Step.EXIT);
//
//		_helper_open_new_map();
//		_helper_click_ok_if_dialog_blocks();
//
//		_helper_reload_graph_or_fail(0);
//
//		_helper_click_on_menu(MENU_EXIT);
//
////		_helper_validate_unsaved_changes_warning_and_cancel();
//		_helper_click_cancel_if_dialog_blocks();
//
//		_helper_click_on_menu(MENU_SAVE_FILE);
//
//		_helper_add_place("Node1", 100, 100);
//
//		_helper_click_on_menu(MENU_EXIT);
//
//		_helper_validate_unsaved_changes_warning_and_cancel();
//		_helper_click_cancel_if_dialog_blocks();
//		_helper_click_on_menu(MENU_SAVE_FILE);
//
//		_helper_add_place("Node2", 200, 200);
//
//		_helper_click_on_menu(MENU_EXIT);
//
//		_helper_validate_unsaved_changes_warning_and_cancel();
//		_helper_click_cancel_if_dialog_blocks();
//		_helper_click_on_menu(MENU_SAVE_FILE);
//
//		var node1 = _helper_get_node_by_id("#Node1");
//		var node2 = _helper_get_node_by_id("#Node2");
//		_helper_select(node1);
//		_helper_select(node2);
//		_helper_add_edge("Node1->Node2", 5);
//
//		_helper_click_on_menu(MENU_EXIT);
//
//		_helper_validate_unsaved_changes_warning_and_cancel();
//		_helper_click_cancel_if_dialog_blocks();
//		_helper_click_on_menu(MENU_SAVE_FILE);
//
//		_helper_click_on_menu(MENU_EXIT);
//
//		_test_ending(Step.EXIT);
//	}
//
//	@Test
//	@Disabled
//	@Order(1000)
//	void test20_auto_fail() {
//		fail("Testerna är inte färdigutvecklade och därför misslyckas alltid det sista steget för att inte felaktiga resultat ska registreras.");
//	}
//
//	enum Step {
//		INIT, IDS, TITLE, NEW_MAP, NEW_PLACE, NEW_CONN, OPEN, SAVE, SAVE_IMG, FIND_PATH, SHOW_CONN, CHG_CONN, EXIT
//	}
//}