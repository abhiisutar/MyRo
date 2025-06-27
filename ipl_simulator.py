import random
from typing import List, Dict

class Team:
    def __init__(self, name: str, players: List[str]):
        self.name = name
        self.players = players
        self.points = 0
        self.matches_played = 0
        self.wins = 0
        self.losses = 0

class IPLSimulator:
    def __init__(self):
        self.teams = {
            "CSK": Team("Chennai Super Kings", ["MS Dhoni", "Ravindra Jadeja", "Devon Conway"]),
            "MI": Team("Mumbai Indians", ["Rohit Sharma", "Hardik Pandya", "Ishan Kishan"]),
            "RCB": Team("Royal Challengers Bangalore", ["Virat Kohli", "Glenn Maxwell", "Faf du Plessis"]),
            "KKR": Team("Kolkata Knight Riders", ["Shreyas Iyer", "Andre Russell", "Sunil Narine"]),
            "RR": Team("Rajasthan Royals", ["Sanju Samson", "Jos Buttler", "Trent Boult"]),
            "PBKS": Team("Punjab Kings", ["Shikhar Dhawan", "Sam Curran", "Kagiso Rabada"]),
            "DC": Team("Delhi Capitals", ["David Warner", "Mitchell Marsh", "Axar Patel"]),
            "SRH": Team("Sunrisers Hyderabad", ["Aiden Markram", "Bhuvneshwar Kumar", "Heinrich Klaasen"]),
        }
    
    def simulate_match(self, team1: Team, team2: Team) -> tuple[Team, Team]:
        # Simulate toss
        toss = random.choice([team1, team2])
        
        # Simulate match result with some randomness
        team1_score = random.randint(120, 220)
        team2_score = random.randint(120, 220)
        
        winner = team1 if team1_score > team2_score else team2
        loser = team2 if team1_score > team2_score else team1
        
        # Update stats
        winner.points += 2
        winner.wins += 1
        winner.matches_played += 1
        loser.matches_played += 1
        loser.losses += 1
        
        return winner, loser

    def simulate_league(self):
        print("\n=== IPL 2025 Season Simulation ===\n")
        team_list = list(self.teams.values())
        
        # Each team plays against every other team twice
        for i in range(len(team_list)):
            for j in range(i + 1, len(team_list)):
                # Two matches between each pair
                for _ in range(2):
                    team1, team2 = team_list[i], team_list[j]
                    winner, loser = self.simulate_match(team1, team2)
                    print(f"{winner.name} defeated {loser.name}")
        
        print("\n=== Final Points Table ===")
        sorted_teams = sorted(team_list, key=lambda x: (x.points, x.wins), reverse=True)
        print("\nTeam                          M   W   L   Pts")
        print("------------------------------------------------")
        for team in sorted_teams:
            print(f"{team.name:<28} {team.matches_played:>3} {team.wins:>3} {team.losses:>3} {team.points:>4}")
        
        print(f"\nCongratulations! {sorted_teams[0].name} wins IPL 2025!")

if __name__ == "__main__":
    # Create and run simulation
    ipl = IPLSimulator()
    ipl.simulate_league()
