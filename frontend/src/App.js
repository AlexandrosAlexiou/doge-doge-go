import "./App.css";
import Home from "./pages/Home";
import Lucky from "./pages/Lucky";
import SearchPage from "./pages/SearchPage";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {
    return (
        <div className="App">
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/search" element={<SearchPage />} />
                    <Route path="/lucky" element={<Lucky />} />
                </Routes>
            </Router>
        </div>
    );
}

export default App;
