# ♟️ Chess with Live Position Analysis

**A desktop chess game — with selectable board topologies and real-time engine analysis — built in Java following Clean Architecture.**

Play a full game of chess in a Swing window while a side panel continuously analyzes
the position: best move, evaluation, and win percentage, powered by an external chess
engine. Choose a classic board or a "wrap-around" board where pieces travel off one
edge and reappear on the other.

> This is a group project for **CSC207: Software Design** at the University of Toronto.

---

## Table of Contents

1. [Purpose](#purpose)
2. [Authors and Contributors](#authors-and-contributors)
3. [Features](#features)
4. [Requirements](#requirements)
5. [Installation](#installation)
6. [Usage Guide](#usage-guide)
7. [Project Structure](#project-structure)
8. [Running the Tests](#running-the-tests)
9. [Troubleshooting](#troubleshooting)
10. [License](#license)
11. [Feedback](#feedback)
12. [Contributing](#contributing)

---

## Purpose

Chess engines and analysis boards are usually locked behind polished commercial apps,
and almost none of them let you experiment with **non-standard board rules**. This
project was built to do two things:

- **Learn Clean Architecture in practice.** The codebase is a teaching vehicle for
  CSC207: every feature is split into clearly separated layers (entities, use cases,
  interface adapters, and views) so responsibilities never leak across boundaries.
- **Make position analysis a first-class, live feature.** Instead of copying a FEN
  string into a website, the analysis updates automatically as you play — and it works
  even on the custom "wrap-around" board.

If you want to play chess, tinker with alternate board topologies, or study a working
Clean Architecture + Observer-pattern Java application, this project is for you.

---

## Authors and Contributors

| Contributor       | Primary area                                                         |
|-------------------|----------------------------------------------------------------------|
| **Sean Dursi**    | Analysis feature (engine integration, FEN translation, presentation) |
| **Yucheng Shi**   | Move-making and game logic                                           |
| **Roan Reynolds** | Move-making and game logic                                           |
| **Zhibo Sheng**   | Archive feature (persistence, past games replay)                     |

Contributions are visible in the project's Git history.

---

## Features

### 1. Full chess gameplay
- Click-to-select, click-to-move piece interaction on an 8×8 board.
- Complete move validation, including **castling**, **en passant**, and pawn logic.
- **Checkmate detection** — when the side to move has no legal moves, the game
  announces the winner.

### 2. Selectable board topologies
On launch, a menu lets you pick how the board's edges behave:

| Option | Behaviour |
| --- | --- |
| **Standard Chess (0)** | Classic rules — the board edges are walls. |
| **Wrap-around (1)** | Pieces that move off one vertical edge reappear on the opposite side. |

### 3. Live position analysis
A panel on the right continuously analyzes the current position and displays:
- **Best move** (e.g. `e2 -> e4`)
- **Evaluation** (centipawn score from the side-to-move's perspective)
- **Win chance** (percentage)

The analysis runs on a background thread, so the board never freezes while it waits
for the engine. Example of a rendered analysis message:

```text
== WHITE'S TURN  (1) ==

White WinChance: 39.0%
White Eval: 0.49
Best Move: e4 -> e5
```

- **Message History** button — shows every analysis produced so far.
- **Return** button — collapses back to just the most recent analysis.

### 4. Clean Architecture + Observer pattern
When a move is made, the move feature fires an update signal that the analysis feature
observes (via `java.beans.PropertyChangeListener`) and re-analyzes the new position —
with **no direct dependency** from one feature's inner logic on the other.

### 5. Replay past games
Games are **automatically saved** to the game archive (a remote **PostgreSQL** database) upon 
finishing. The game archive shows a list of creation times and final results for the completed games
the user have played, and the user can select the game to replay.

In the replay page, there are **interactive forward and back arrow keys** to step through the moves.
 **Position analysis** will also be available during replay.

As in the move feature, the replay feature also fires an update signal to the analysis feature via
`java.beans.PropertyChangeListener` to analyze the new position. 

---

## Requirements

| Software | Version | Notes / Link |
| --- | --- | --- |
| **Java Development Kit (JDK)** | **16** (source/target set to 16) | [Download JDK](https://adoptium.net/) |
| **Apache Maven** | 3.6+ | [Install Maven](https://maven.apache.org/install.html) (bundled with IntelliJ IDEA) |
| **Internet connection** | — | Required at runtime; analysis calls the [chess-api.com](https://chess-api.com/) engine. |

**Dependencies** (downloaded automatically by Maven — no manual install needed):

| Dependency | Version  | Purpose                       | Link |
| --- |----------|-------------------------------| --- |
| Gson | 2.11.0   | Parse the JSON returned by the analysis API | [gson](https://github.com/google/gson) |
| JUnit 4 | 4.13.1   | Legacy unit tests             | [junit4](https://junit.org/junit4/) |
| JUnit Jupiter (JUnit 5) | 5.14.0   | Unit tests                    | [junit5](https://junit.org/junit5/) |
| Mockito | 5.13.0   | Mocking in tests              | [mockito](https://site.mockito.org/) |
| JaCoCo (plugin) | 0.8.12   | Test coverage reports         | [jacoco](https://www.jacoco.org/jacoco/) |
| PostgreSQL JDBC Driver| 42.7.13  | PostgreSQL database Connection| [posgresql](https://jdbc.postgresql.org)|

**Operating system:** Cross-platform. The game runs anywhere a Java 16 JVM with a
graphical display is available (Windows, macOS, and Linux). It requires a desktop
environment because the UI is built with Java Swing.

---

## Installation

The recommended path is IntelliJ IDEA, which the course uses and which bundles Maven.

### Option A — IntelliJ IDEA (recommended)

1. Clone the repository:
   ```bash
   git clone <your-repository-url>
   cd group_project
   ```
2. Open the folder in **IntelliJ IDEA** (`File ▸ Open…` and select the project folder).
3. IntelliJ detects `pom.xml` and imports it as a Maven project. Wait for it to finish
   downloading the dependencies listed above.
4. Ensure the project SDK is set to **JDK 16**
   (`File ▸ Project Structure ▸ Project ▸ SDK`).

### Option B — Command line (Maven)

1. Clone the repository (as above) and `cd group_project`.
2. Compile the project and download dependencies:
   ```bash
   mvn compile
   ```

---

## Usage Guide

### Running the app

**From IntelliJ:** open `src/main/java/app/main.java`, then run the `Main` class
(green ▶ next to `public static void main`).

The app starts with a topology menu:

1. **Choose a board topology** — click **Standard Chess (0)** or **Wrap-around (1)**.
2. The game window opens with the **board on the left** and the **analysis panel on
   the right**. The opening position is analyzed automatically.

### Playing a move

1. **Click a piece** to select it.
2. **Click a destination square** to move it (illegal moves are ignored).
3. To deselect, click the selected square again.
4. After each move, the analysis panel refreshes with the engine's take on the new
   position.

### Reading the analysis

- The panel always shows the latest analysis by default.
- Click **Message History** to see the full running list of analyses.
- Click **Return** to go back to only the most recent message.

### Rewatch a game

1. Select **Archive** on the main menu to go to the archive page
2. On the archive page, select the game to rewatch
3. Use the **Forward** and **Backward** buttons to navigate through moves
4. Read **analysis** on the side panel

---

## Project Structure

The code follows **Clean Architecture**, with dependencies pointing inward
(views → controllers → use cases → entities). Each feature is self-contained.

```
src/main/java/
├── app/                 # Assembly & entry point
│   ├── main.java        # Topology menu, builds the game
│   └── AppBuilder.java  # Wires each feature's CA stack into one window
├── entity/              # Core game rules (Board, GameState, Move validation, ...)
├── archive.use_case/            # Make-move application logic (MakeMoveInteractor, boundaries)
├── archive.interface_adapter/   # Move controller / presenter / view model
├── view/                # MoveView (the chess board UI)
└── Analysis/            # Analysis feature (packaged by feature)
    ├── AnalyzeMoveInteractor.java   # Use case: turns a board into an analysis
    ├── BoardToFenTranslator.java    # Converts a Board into a FEN string
    ├── ChessApiAdapter.java         # Calls chess-api.com
    ├── AnalyzeController / Presenter / ViewModel / View
    └── ...boundaries & data objects
```

**Data flow for a single analysis:**

```
MakeMoveInteractor  --(fires "update-analysis")-->  AnalyzeMoveInteractor
        │                                                   │
        │                                    BoardToFenTranslator (Board → FEN)
        │                                                   │
        │                                    ChessApiAdapter → chess-api.com
        │                                                   │
        │                            AnalyzePresenter → AnalyzeViewModel → AnalyzeView
```

---

## Running the Tests

Run the full test suite with Maven:

```bash
mvn test
```

Tests use **JUnit 5** and **Mockito** (the analysis engine is mocked, so most tests run
offline and deterministically). After a test run, JaCoCo writes a coverage report to:

```
target/site/jacoco/index.html
```

Open that file in a browser to view line and branch coverage.

---

## Troubleshooting

| Problem | Cause | Fix |
| --- | --- | --- |
| The analysis panel stays blank | No internet, or the engine is unreachable | Confirm you can reach [chess-api.com](https://chess-api.com/); analysis needs a live connection. |
| A red stack trace mentioning the analysis appears in the console | The engine rejected a position | Re-run; if it persists, please [report it](#feedback) with the console output. |
| `invalid target release: 16` when building | Your JDK is older than 16 | Install **JDK 16+** and point the project/IDE at it. |
| Old behaviour after editing code in IntelliJ | Stale compiled classes | `Build ▸ Rebuild Project`, then run again. |

---

## License

This project is coursework created for **CSC207 at the University of Toronto** and is
shared for **educational purposes**.

At present the repository does not include a formal open-source license file, which
means default copyright applies and reuse rights are not granted. If you intend to make
this project reusable, add a `LICENSE` file (for example, the
[MIT License](https://choosealicense.com/licenses/mit/)) and state it here so the README
and the repository stay consistent.

---

## Feedback

We welcome bug reports, questions, and suggestions.

- **How:** Open an issue on the project's GitHub **Issues** page.
- **What makes a valid report:** a clear title, the steps to reproduce, what you
  expected, what actually happened, and — if relevant — the console output and your
  OS/JDK version. One issue per topic.
- **What to expect:** issues are triaged by the contributors listed above. A
  maintainer will typically respond to confirm, ask for details, or explain next steps.
  Since this is a course project, response times may vary.

---

## Contributing

Contributions are welcome via GitHub pull requests.

1. **Fork** the repository (click **Fork** on the GitHub page to create your own copy).
2. **Clone** your fork and create a branch for your change:
   ```bash
   git clone <your-fork-url>
   cd group_project
   git checkout -b feature/short-description
   ```
3. **Make your change.** Keep features within Clean Architecture boundaries, add or
   update tests, and make sure `mvn test` passes.
4. **Commit** with a clear message and **push** to your fork:
   ```bash
   git commit -m "Add: short description of the change"
   git push origin feature/short-description
   ```
5. **Open a Pull Request** against the main repository. In the PR description, explain
   **what** changed and **why**, and link any related issue.

**A good merge request:** is focused on one thing, keeps the diff small and readable,
includes tests for new behaviour, and does not break existing tests.

**Review process:** at least one contributor reviews each PR. Reviewers check that the
change respects the architecture, that tests pass, and that the code is clear. Once
approved, a maintainer merges it into the main branch.
