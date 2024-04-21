import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataStreams extends JFrame {

    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;
    private JButton loadButton;
    private JButton searchButton;
    private JButton quitButton;
    private String currentFilePath;

    public DataStreams() {
        super("Data Stream Processing");
        initializeComponents();
        layoutComponents();
        addActionListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void initializeComponents() {
        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);

        searchField = new JTextField(20);
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        searchButton.setEnabled(false); // Enable only after a file is loaded
        quitButton = new JButton("Quit");
    }

    private void layoutComponents() {
        JPanel filePanel = new JPanel(new FlowLayout());
        filePanel.add(loadButton);
        filePanel.add(searchField);
        filePanel.add(searchButton);
        filePanel.add(quitButton);

        JPanel textAreaPanel = new JPanel(new GridLayout(1, 2));
        textAreaPanel.add(new JScrollPane(originalTextArea));
        textAreaPanel.add(new JScrollPane(filteredTextArea));

        getContentPane().add(filePanel, BorderLayout.NORTH);
        getContentPane().add(textAreaPanel, BorderLayout.CENTER);
    }

    private void addActionListeners() {
        loadButton.addActionListener(this::loadFile);
        searchButton.addActionListener(this::filterText);
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void loadFile(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            try (Stream<String> lines = Files.lines(Paths.get(currentFilePath))) {
                originalTextArea.setText("");
                lines.forEach(line -> originalTextArea.append(line + "\n"));
                searchButton.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterText(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        try (Stream<String> lines = Files.lines(Paths.get(currentFilePath))) {
            filteredTextArea.setText("");
            lines.filter(line -> line.toLowerCase().contains(searchText))
                    .forEach(line -> filteredTextArea.append(line + "\n"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to filter text: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreams::new);
    }
}
