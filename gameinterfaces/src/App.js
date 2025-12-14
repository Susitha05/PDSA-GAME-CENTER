// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal'
import GameDisplay from './pages/game_display'
import Tsp from './pages/tsp'
import Traffic from './components/PlayerRegistration'
function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
            <Route path="/gameDisplay" element={<GameDisplay />}/>
            <Route path="/tspGame" element={<Tsp />}/>
            <Route path="/trc" element={<Traffic />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
