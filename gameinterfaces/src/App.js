// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal'
import GameDisplay from './pages/game_display'
function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
            <Route path="/gameDisplay" element={<GameDisplay />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
