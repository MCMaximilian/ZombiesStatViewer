package com.mcmaximilian;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static String uuid;

    public static void main(String[] args) {

        for ( int i =0; i < 100000000; i++ ) {

            System.out.println("Input your minecraft nickname!");

            Scanner scanner  = new Scanner(System.in);
            String MCNickName = scanner.nextLine();
            String input = scanner.nextLine().toLowerCase();


            try {
                String uuidURL = "https://api.mojang.com/users/profiles/minecraft/" + MCNickName;
                String fileName = MCNickName.toLowerCase() + ".json";
                URL website = new URL(uuidURL);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);

                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);  // download?
                fos.close();
            } catch (Exception e) {
                System.out.println("Please input correct mc-nickname.");
                String path = System.getProperty("user.dir");
                File root = new File(path);
                File[] files = root.listFiles();
                for ( File fileName : Objects.requireNonNull(files)) {
                    String name = fileName.getName();
                    String a = name.substring(name.lastIndexOf(".")+1).toLowerCase();
                    if( a.contains("json") ){
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            fileName.delete();
                        } catch (Exception ex) {
                            System.out.println("non-json file here.");
                        }
                    }
                }
                break;
            }


            JSONParser jsonParser = new JSONParser();
            try {
                Reader reader = new FileReader(MCNickName.toLowerCase() + ".json");
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                uuid = (String) jsonObject.get("id");
                reader.close();

            } catch (IOException | ParseException e) {
                System.out.println("get bug when JSON reading" + Color.RED);
            }


            String playerURL = "https://api.hypixel.net/player?key=076a5a08-8b1e-4328-86de-073877b54849&uuid=" + uuid;

            try {
                String fileName = "player.json";
                URL website = new URL(playerURL);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);

                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);  // download?
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            try {
                Reader reader = new FileReader("player.json");
                JSONObject file = (JSONObject) jsonParser.parse(reader);
                JSONObject player = (JSONObject) file.get("player");
                JSONObject stats = (JSONObject) player.get("stats");
                JSONObject arcade = (JSONObject) stats.get("Arcade");

                Long knocked_down_count;
                if (input.equalsIgnoreCase("-s")) {
                    Long bullet_hit_zombie = (Long) arcade.get("bullets_hit_zombies");
                    Long death_all_map = (Long) arcade.get("deaths_zombies");
                    Long door_open_count = (Long) arcade.get("doors_opened_zombies");
                    Long headshot_count = (Long) arcade.get("headshots_zombies");
                    knocked_down_count = (Long) arcade.get("times_knocked_down_zombies");
                    Long total_survive_round_count = (Long) arcade.get("total_rounds_survived_zombies");
                    Long zombie_kills_count = (Long) arcade.get("zombie_kills_zombies");
                    Long best_round = (Long) arcade.get("best_round_zombies");
                    Long window_repaired_count = (Long) arcade.get("windows_repaired_zombies");
                    Long revive_count = (Long) arcade.get("players_revived_zombies");
                    Long win_count = (Long) arcade.get("wins_zombies");
                    Long bullet_shoot_count = (Long) arcade.get("bullets_shot_zombies");
                    Long Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies");
                    Long Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies");
                    Long Fastest_Win = (Long) arcade.get("fastest_time_30_zombies");

                    BigDecimal a = new BigDecimal(bullet_shoot_count);
                    BigDecimal b = new BigDecimal(bullet_hit_zombie);
                    BigDecimal c = new BigDecimal(headshot_count);

                    BigDecimal Bullet_Accuracy = b.divide(a, 2, RoundingMode.DOWN);
                    BigDecimal Head_Accuracy = c.divide(b, 2, RoundingMode.DOWN);

                    JSONObject jsonObject = (JSONObject) arcade.get("leaderboardSettings");
                    String dummy = (String) jsonObject.get("mode");
                    String mainMap = null;
                    if (dummy.equalsIgnoreCase("ZOMBIES_ALIEN_ARCADIUM")) {
                        mainMap = "Alien Arcadium";
                    } else if (dummy.equalsIgnoreCase("ZOMBIES_BAD_BLOOD")) {
                        mainMap = "Bad Blood";
                    } else if (dummy.equalsIgnoreCase("ZOMBIES_DEAD_END")) {
                        mainMap = "Dead End";
                    }

                    reader.close();

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Bullet Shoot Count: " + bullet_shoot_count);
                    System.out.println("Bullet Hit Zombies: " + bullet_hit_zombie);
                    System.out.println("Bullet Accuracy: " + Bullet_Accuracy);
                    System.out.println("Head-Shot Accuracy: " + Head_Accuracy);
                    System.out.println("Total Survive Round: " + total_survive_round_count);
                    System.out.println("Best Round: " + best_round);
                    System.out.println("Wins: " + win_count);
                    System.out.println("Fastest Round 10: " + GetTime(Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(Fastest_Win));
                    System.out.println("Zombie Kills: " + zombie_kills_count);
                    System.out.println("Window Repaired Count: " + window_repaired_count);
                    System.out.println("Door Opens: " + door_open_count);
                    System.out.println("Revive Counts: " + revive_count);
                    System.out.println("Knocked-Down Count: " + knocked_down_count);
                    System.out.println("Deaths: " + death_all_map);
                    System.out.println("Main Map: " + mainMap);

                    System.out.println();
                    System.out.println(MCNickName + "'s Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                } else if (input.equalsIgnoreCase("-bb")) {
                    Long BadBlood_Death = (Long) arcade.get("deaths_zombies_badblood");
                    Long BadBlood_Open_Door = (Long) arcade.get("doors_opened_zombies_badblood");
                    Long BadBlood_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_badblood");
                    Long BadBlood_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_badblood");
                    Long BadBlood_Zombie_Kills = (Long) arcade.get("zombie_kills_zombies_badblood");
                    Long BadBlood_Best_Round = (Long) arcade.get("best_round_zombies_badblood");
                    Long BadBlood_Window_Fix = (Long) arcade.get("windows_repaired_zombies_badblood");
                    Long BadBlood_Player_Revive = (Long) arcade.get("players_revived_zombies_badblood");
                    Long BadBlood_King_Slime_Kill = (Long) arcade.get("king_slime_zombie_kills_zombies");
                    Long BadBlood_Wither_kill = (Long) arcade.get("wither_zombie_kills_zombies");
                    Long BadBlood_Herobrine_Kill = (Long) arcade.get("herobrine_zombie_zombie_kills_zombies");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + BadBlood_Best_Round);
                    System.out.println("Total Rounds: " + BadBlood_Total_Rounds);
                    System.out.println("Open Door Count: " + BadBlood_Open_Door);
                    System.out.println("Zombie Kills: " + BadBlood_Zombie_Kills);
                    System.out.println("King Slime Kills: " + BadBlood_King_Slime_Kill);
                    System.out.println("Wither Kills: " + BadBlood_Wither_kill);
                    System.out.println("Herobrine Kills: " + BadBlood_Herobrine_Kill);
                    System.out.println("Window Fix Count: " + BadBlood_Window_Fix);
                    System.out.println("Player Revive Count: " + BadBlood_Player_Revive);
                    System.out.println("Knocked-Down Count: " + BadBlood_Knocked_Down);
                    System.out.println("Deaths: " + BadBlood_Death);

                    System.out.println();
                    System.out.println(MCNickName + "'s Bad Blood Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();
                } else if (input.equalsIgnoreCase("-de normal")) {

                    Long DeadEnd_Normal_Best_Round = (Long) arcade.get("best_round_zombies_deadend_normal");
                    Long DeadEnd_Normal_DeathCount = (Long) arcade.get("deaths_zombies_deadend_normal");
                    Long DeadEnd_Normal_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_deadend_normal");
                    Long DeadEnd_Normal_Door_Open = (Long) arcade.get("doors_opened_zombies_deadend_normal");
                    Long DeadEnd_Normal_Window_Fix = (Long) arcade.get("windows_repaired_zombies_deadend_normal");
                    Long DeadEnd_Normal_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_deadend_normal");
                    Long DeadEnd_Normal_Round10_Fast = (Long) arcade.get("fastest_time_10_zombies_deadend_normal");
                    Long DeadEnd_Normal_Round20_Fast = (Long) arcade.get("fastest_time_20_zombies_deadend_normal");
                    Long DeadEnd_Normal_Win_Fast = (Long) arcade.get("fastest_time_30_zombies_deadend_normal");
                    Long DeadEnd_Normal_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_deadend_normal");
                    Long DeadEnd_Normal_Player_Revive = (Long) arcade.get("players_revived_zombies_deadend_normal");
                    Long DeadEnd_Normal_Wins = (Long) arcade.get("wins_zombies_deadend_normal");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + DeadEnd_Normal_Best_Round);
                    System.out.println("Total Survive Rounds: " + DeadEnd_Normal_Total_Rounds);
                    System.out.println("Wins: " + DeadEnd_Normal_Wins);
                    System.out.println("Zombie Kills: " + DeadEnd_Normal_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(DeadEnd_Normal_Round10_Fast));
                    System.out.println("Fastest Round 20: " + GetTime(DeadEnd_Normal_Round20_Fast));
                    System.out.println("Fastest Win: " + GetTime(DeadEnd_Normal_Win_Fast));
                    System.out.println("Door Open Count: " + DeadEnd_Normal_Door_Open);
                    System.out.println("Window Fix Count: " + DeadEnd_Normal_Window_Fix);
                    System.out.println("Player Revive Count: " + DeadEnd_Normal_Player_Revive);
                    System.out.println("Knocked-Down Count: " + DeadEnd_Normal_Knocked_Down);
                    System.out.println("Deaths: " + DeadEnd_Normal_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Dead End Normal Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();

                } else if (input.equalsIgnoreCase("-de hard")) {

                    Long DeadEnd_Hard_Best_Round = (Long) arcade.get("best_round_zombies_deadend_hard");
                    Long DeadEnd_Hard_DeathCount = (Long) arcade.get("deaths_zombies_deadend_hard");
                    Long DeadEnd_Hard_Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies_deadend_hard");
                    Long DeadEnd_Hard_Player_Revive = (Long) arcade.get("players_revived_zombies_deadend_hard");
                    Long DeadEnd_Hard_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_deadend_hard");
                    Long DeadEnd_Hard_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_deadend_hard");
                    Long DeadEnd_Hard_Window_Fix = (Long) arcade.get("windows_repaired_zombies_deadend_hard");
                    Long DeadEnd_Hard_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_deadend_hard");
                    Long DeadEnd_Hard_Door_Opens = (Long) arcade.get("doors_opened_zombies_deadend_hard");
                    Long DeadEnd_Hard_Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies_deadend_hard");
                    Long DeadEnd_Hard_Fastest_Win = (Long) arcade.get("fastest_time_30_zombies_deadend_hard");
                    Long DeadEnd_Hard_Wins = (Long) arcade.get("wins_zombies_deadend_hard");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + DeadEnd_Hard_Best_Round);
                    System.out.println("Total Survive Rounds: " + DeadEnd_Hard_Total_Rounds);
                    System.out.println("Wins: " + DeadEnd_Hard_Wins);
                    System.out.println("Zombie Kills: " + DeadEnd_Hard_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(DeadEnd_Hard_Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(DeadEnd_Hard_Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(DeadEnd_Hard_Fastest_Win));
                    System.out.println("Door Open Count: " + GetTime(DeadEnd_Hard_Door_Opens));
                    System.out.println("Window Fix Count: " + DeadEnd_Hard_Window_Fix);
                    System.out.println("Player Revive Count: " + DeadEnd_Hard_Player_Revive);
                    System.out.println("Knocked-Down Count: " + DeadEnd_Hard_Knocked_Down);
                    System.out.println("Deaths: " + DeadEnd_Hard_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Dead End Hard Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();

                } else if (input.equalsIgnoreCase("-de rip")) {
                    Long DeadEnd_RIP_Best_Round = (Long) arcade.get("best_round_zombies_deadend_rip");
                    Long DeadEnd_RIP_DeathCount = (Long) arcade.get("deaths_zombies_deadend_rip");
                    Long DeadEnd_RIP_Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies_deadend_rip");
                    Long DeadEnd_RIP_Player_Revive = (Long) arcade.get("players_revived_zombies_deadend_rip");
                    Long DeadEnd_RIP_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_deadend_rip");
                    Long DeadEnd_RIP_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_deadend_rip");
                    Long DeadEnd_RIP_Window_Fix = (Long) arcade.get("windows_repaired_zombies_deadend_rip");
                    Long DeadEnd_RIP_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_deadend_rip");
                    Long DeadEnd_RIP_Door_Opens = (Long) arcade.get("doors_opened_zombies_deadend_rip");
                    Long DeadEnd_RIP_Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies_deadend_rip");
                    Long DeadEnd_RIP_Fastest_Win = (Long) arcade.get("fastest_time_30_zombies_deadend_rip");
                    Long DeadEnd_RIP_Wins = (Long) arcade.get("wins_zombies_deadend_rip");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + DeadEnd_RIP_Best_Round);
                    System.out.println("Total Survive Rounds: " + DeadEnd_RIP_Total_Rounds);
                    System.out.println("Wins: " + DeadEnd_RIP_Wins);
                    System.out.println("Zombie Kills: " + DeadEnd_RIP_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(DeadEnd_RIP_Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(DeadEnd_RIP_Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(DeadEnd_RIP_Fastest_Win));
                    System.out.println("Door Open Count: " + GetTime(DeadEnd_RIP_Door_Opens));
                    System.out.println("Window Fix Count: " + DeadEnd_RIP_Window_Fix);
                    System.out.println("Player Revive Count: " + DeadEnd_RIP_Player_Revive);
                    System.out.println("Knocked-Down Count: " + DeadEnd_RIP_Knocked_Down);
                    System.out.println("Deaths: " + DeadEnd_RIP_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Dead End RIP Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();
                } else if (input.equalsIgnoreCase("-de")) {
                    Long Bad_Blood_Death = (Long) arcade.get("deaths_zombies_deadend");
                    Long DeadEnd_Open_Door = (Long) arcade.get("doors_opened_zombies_deadend");
                    Long DeadEnd_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_deadend");
                    Long DeadEnd_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_deadend");
                    Long DeadEnd_Zombie_Kills = (Long) arcade.get("zombie_kills_zombies_deadend");
                    Long DeadEnd_Best_Round = (Long) arcade.get("best_round_zombies_deadend");
                    Long DeadEnd_Window_Fix = (Long) arcade.get("windows_repaired_zombies_deadend");
                    Long DeadEnd_Player_Revive = (Long) arcade.get("players_revived_zombies_deadend");
                    Long DeadEnd_Bombie_Kill = (Long) arcade.get("tnt_zombie_kills_zombies");
                    Long DeadEnd_Inferno_kill = (Long) arcade.get("inferno_zombie_kills_zombies");
                    Long DeadEnd_BroodMother_Kill = (Long) arcade.get("broodmother_zombie_kills_zombies");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + DeadEnd_Best_Round);
                    System.out.println("Total Rounds: " + DeadEnd_Total_Rounds);
                    System.out.println("Open Door Count: " + DeadEnd_Open_Door);
                    System.out.println("Zombie Kills: " + DeadEnd_Zombie_Kills);
                    System.out.println("Bombie Kills: " + DeadEnd_Bombie_Kill);
                    System.out.println("Inferno Kills: " + DeadEnd_Inferno_kill);
                    System.out.println("Broodmother Kills: " + DeadEnd_BroodMother_Kill);
                    System.out.println("Window Fix Count: " + DeadEnd_Window_Fix);
                    System.out.println("Player Revive Count: " + DeadEnd_Player_Revive);
                    System.out.println("Knocked-Down Count: " + DeadEnd_Knocked_Down);
                    System.out.println("Deaths: " + Bad_Blood_Death);

                    System.out.println();
                    System.out.println(MCNickName + "'s Dead End Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();
                } else if (input.equalsIgnoreCase("-bb normal")) {

                    Long BadBlood_Normal_Best_Round = (Long) arcade.get("best_round_zombies_badblood_normal");
                    Long BadBlood_Normal_DeathCount = (Long) arcade.get("deaths_zombies_badblood_normal");
                    Long BadBlood_Normal_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_badblood_normal");
                    Long BadBlood_Normal_Door_Open = (Long) arcade.get("doors_opened_zombies_badblood_normal");
                    Long BadBlood_Normal_Window_Fix = (Long) arcade.get("windows_repaired_zombies_badblood_normal");
                    Long BadBlood_Normal_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_badblood_normal");
                    Long BadBlood_Normal_Round10_Fast = (Long) arcade.get("fastest_time_10_zombies_badblood_normal");
                    Long BadBlood_Normal_Round20_Fast = (Long) arcade.get("fastest_time_20_zombies_badblood_normal");
                    Long BadBlood_Normal_Win_Fast = (Long) arcade.get("fastest_time_30_zombies_badblood_normal");
                    Long BadBlood_Normal_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_badblood_normal");
                    Long BadBlood_Normal_Player_Revive = (Long) arcade.get("players_revived_zombies_badblood_normal");
                    Long BadBlood_Normal_Wins = (Long) arcade.get("wins_badblood_deadend_normal");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + BadBlood_Normal_Best_Round);
                    System.out.println("Total Survive Rounds: " + BadBlood_Normal_Total_Rounds);
                    System.out.println("Wins: " + BadBlood_Normal_Wins);
                    System.out.println("Zombie Kills: " + BadBlood_Normal_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(BadBlood_Normal_Round10_Fast));
                    System.out.println("Fastest Round 20: " + GetTime(BadBlood_Normal_Round20_Fast));
                    System.out.println("Fastest Win: " + GetTime(BadBlood_Normal_Win_Fast));
                    System.out.println("Door Open Count: " + BadBlood_Normal_Door_Open);
                    System.out.println("Window Fix Count: " + BadBlood_Normal_Window_Fix);
                    System.out.println("Player Revive Count: " + BadBlood_Normal_Player_Revive);
                    System.out.println("Knocked-Down Count: " + BadBlood_Normal_Knocked_Down);
                    System.out.println("Deaths: " + BadBlood_Normal_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Bad Blood Normal Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();

                } else if (input.equalsIgnoreCase("-bb hard")) {

                    Long BadBlood_Hard_Best_Round = (Long) arcade.get("best_round_zombies_badblood_hard");
                    Long BadBlood_Hard_DeathCount = (Long) arcade.get("deaths_zombies_badblood_hard");
                    Long BadBlood_Hard_Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies_badblood_hard");
                    Long BadBlood_Hard_Player_Revive = (Long) arcade.get("players_revived_zombies_badblood_hard");
                    Long BadBlood_Hard_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_badblood_hard");
                    Long BadBlood_Hard_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_badblood_hard");
                    Long BadBlood_Hard_Window_Fix = (Long) arcade.get("windows_repaired_zombies_badblood_hard");
                    Long BadBlood_Hard_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_badblood_hard");
                    Long BadBlood_Hard_Door_Opens = (Long) arcade.get("doors_opened_zombies_badblood_hard");
                    Long BadBlood_Hard_Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies_badblood_hard");
                    Long BadBlood_Hard_Fastest_Win = (Long) arcade.get("fastest_time_30_zombies_badblood_hard");
                    Long BadBlood_Hard_Wins = (Long) arcade.get("wins_zombies_badblood_hard");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + BadBlood_Hard_Best_Round);
                    System.out.println("Total Survive Rounds: " + BadBlood_Hard_Total_Rounds);
                    System.out.println("Wins: " + BadBlood_Hard_Wins);
                    System.out.println("Zombie Kills: " + BadBlood_Hard_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(BadBlood_Hard_Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(BadBlood_Hard_Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(BadBlood_Hard_Fastest_Win));
                    System.out.println("Door Open Count: " + GetTime(BadBlood_Hard_Door_Opens));
                    System.out.println("Window Fix Count: " + BadBlood_Hard_Window_Fix);
                    System.out.println("Player Revive Count: " + BadBlood_Hard_Player_Revive);
                    System.out.println("Knocked-Down Count: " + BadBlood_Hard_Knocked_Down);
                    System.out.println("Deaths: " + BadBlood_Hard_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Bad Blood Hard Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();

                } else if (input.equalsIgnoreCase("-bb rip")) {

                    Long BadBlood_RIP_Best_Round = (Long) arcade.get("best_round_zombies_badblood_rip");
                    Long BadBlood_RIP_DeathCount = (Long) arcade.get("deaths_zombies_badblood_rip");
                    Long BadBlood_RIP_Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies_badblood_rip");
                    Long BadBlood_RIP_Player_Revive = (Long) arcade.get("players_revived_zombies_badblood_rip");
                    Long BadBlood_RIP_Knocked_Down = (Long) arcade.get("times_knocked_down_zombies_badblood_rip");
                    Long BadBlood_RIP_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_badblood_rip");
                    Long BadBlood_RIP_Window_Fix = (Long) arcade.get("windows_repaired_zombies_badblood_rip");
                    Long BadBlood_RIP_Zombies_Kills = (Long) arcade.get("zombie_kills_zombies_badblood_rip");
                    Long BadBlood_RIP_Door_Opens = (Long) arcade.get("doors_opened_zombies_badblood_rip");
                    Long BadBlood_RIP_Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies_badblood_rip");
                    Long BadBlood_RIP_Fastest_Win = (Long) arcade.get("fastest_time_30_zombies_badblood_rip");
                    Long BadBlood_RIP_Wins = (Long) arcade.get("wins_zombies_badblood_rip");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + BadBlood_RIP_Best_Round);
                    System.out.println("Total Survive Rounds: " + BadBlood_RIP_Total_Rounds);
                    System.out.println("Wins: " + BadBlood_RIP_Wins);
                    System.out.println("Zombie Kills: " + BadBlood_RIP_Zombies_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(BadBlood_RIP_Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(BadBlood_RIP_Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(BadBlood_RIP_Fastest_Win));
                    System.out.println("Door Open Count: " + GetTime(BadBlood_RIP_Door_Opens));
                    System.out.println("Window Fix Count: " + BadBlood_RIP_Window_Fix);
                    System.out.println("Player Revive Count: " + BadBlood_RIP_Player_Revive);
                    System.out.println("Knocked-Down Count: " + BadBlood_RIP_Knocked_Down);
                    System.out.println("Deaths: " + BadBlood_RIP_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Bad Blood RIP Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    reader.close();
                } else if (input.equalsIgnoreCase("-aa")) {

                    Long AlienArcadium_Best_Round = (Long) arcade.get("best_round_zombies_alienarcadium");
                    Long AlienArcadium_DeathCount = (Long) arcade.get("deaths_zombies_alienarcadium");
                    Long AlienArcadium_Door_Opens = (Long) arcade.get("doors_opened_zombies_alienarcadium");
                    Long AlienArcadium_Fastest_Round10 = (Long) arcade.get("fastest_time_10_zombies_alienarcadium_normal");
                    Long AlienArcadium_Knocked_Downs = (Long) arcade.get("times_knocked_down_zombies_alienarcadium");
                    Long AlienArcadium_Total_Rounds = (Long) arcade.get("total_rounds_survived_zombies_alienarcadium");
                    Long AlienArcadium_Window_Fix = (Long) arcade.get("windows_repaired_zombies_alienarcadium");
                    Long AlienArcadium_Zombie_Kills = (Long) arcade.get("zombie_kills_zombies_alienarcadium");
                    Long AlienArcadium_Fastest_Round20 = (Long) arcade.get("fastest_time_20_zombies_alienarcadium_normal");
                    Long AlienArcadium_Giant_Kills = (Long) arcade.get("giant_zombie_kills_zombies");
                    Long AlienArcadium_player_revive = (Long) arcade.get("players_revived_zombies_alienarcadium");
                    Long AlienArcadium_o1_kills = (Long) arcade.get("the_old_one_zombie_kills_zombies");
                    Long AlienArcadium_Fastest_Win = (Long) arcade.get("fastest_time_30_zombies_alienarcadium_normal");
                    Long AlienArcadium_Wins = (Long) arcade.get("wins_zombies_alienarcadium");
                    Long AlienArcadium_World_Ender_Kills = (Long) arcade.get("world_ender_zombie_kills_zombies");
                    Long AlienArcadium_RainbowGiant_Kills = (Long) arcade.get("giant_rainbow_zombie_kills_zombies");
                    Long AlienArcadium_Molten_Zombie_Kills = (Long) arcade.get("molten_zombie_kills_zombies");
                    Long AlienArcadium_FireLord_Kills = (Long) arcade.get("fire_lord_zombie_kills_zombies");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println("Best Round: " + AlienArcadium_Best_Round);
                    System.out.println("Total Survive Rounds: " + AlienArcadium_Total_Rounds);
                    System.out.println("Wins: " + AlienArcadium_Wins);
                    System.out.println("Zombie Kills: " + AlienArcadium_Zombie_Kills);
                    System.out.println("The Old One Kills: " + AlienArcadium_o1_kills);
                    System.out.println("World Ender Kills: " + AlienArcadium_World_Ender_Kills);
                    System.out.println("Giant Kills: " + AlienArcadium_Giant_Kills);
                    System.out.println("Rainbow Giant Kills: " + AlienArcadium_RainbowGiant_Kills);
                    System.out.println("Molten Zombie Kills: " + AlienArcadium_Molten_Zombie_Kills);
                    System.out.println("Fire Lord Kills: " + AlienArcadium_FireLord_Kills);
                    System.out.println("Fastest Round 10: " + GetTime(AlienArcadium_Fastest_Round10));
                    System.out.println("Fastest Round 20: " + GetTime(AlienArcadium_Fastest_Round20));
                    System.out.println("Fastest Win: " + GetTime(AlienArcadium_Fastest_Win));
                    System.out.println("Door Open Count: " + AlienArcadium_Door_Opens);
                    System.out.println("Window Fix Count: " + AlienArcadium_Window_Fix);
                    System.out.println("Player Revive Count: " + AlienArcadium_player_revive);
                    System.out.println("Knocked-Down Count: " + AlienArcadium_Knocked_Downs);
                    System.out.println("Deaths: " + AlienArcadium_DeathCount);

                    System.out.println();
                    System.out.println(MCNickName + "'s Alien Arcadium Stat!");

                    System.out.println();
                    System.out.println();
                    System.out.println("<<< Thanks to KeeganZ >>>");

                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();


                    reader.close();
                } else {
                    System.out.println("Input Error");
                    reader.close();
                }
                reader.close();
            } catch (IOException | ParseException e) {
                String path = System.getProperty("user.dir");
                Path namePath = Paths.get(path + "\\" + MCNickName.toLowerCase() + ".json");
                Path playerPath = Paths.get(path + "\\" + "player.json");
                try {
                    Files.deleteIfExists(playerPath);
                    Files.deleteIfExists(namePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            String path = System.getProperty("user.dir");
            Path namePath = Paths.get(path + "\\" + MCNickName.toLowerCase() + ".json");
            Path playerPath = Paths.get(path + "\\" + "player.json");

            try {
                Files.deleteIfExists(namePath);
                Files.deleteIfExists(playerPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("If u want to stop, input \"stop\". And if u want to use it more, enter any value to continue this. ");

            String stop = scanner.nextLine();
            if ( stop.equalsIgnoreCase("stop")) {
                break;
            }
        }
    }

    public static String GetTime( Long sec ) {

        if ( sec == null) {
            return "null";
        }
        else {
            long hours ;
            Long minutes ;
            Long second ;

            hours = sec / 3600;
            sec = sec%3600;
            minutes = sec / 60;
            second = sec%60;

            String min ;
            String secon ;

            if ( minutes < 10) {

                min = String.valueOf(minutes);
                min = "0" + min;

            }
            else {
                min = minutes.toString();
            }

            if ( second < 10) {
                secon = String.valueOf(second);
                secon = "0" + secon;
            }
            else  {
                secon = second.toString();
            }
            return hours + ":" + min + ":" + secon;
        }

    }
}