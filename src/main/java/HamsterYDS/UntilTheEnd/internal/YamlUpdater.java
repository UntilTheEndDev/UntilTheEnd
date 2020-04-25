/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/12 16:29:37
 *
 * UntilTheEnd/UntilTheEnd/YamlUpdater.java
 */

package HamsterYDS.UntilTheEnd.internal;


import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class YamlUpdater {
    public static void merge(
            Map<String, List<String>> latest_commits,
            Map<String, List<String>> latest_area,
            Map<String, List<String>> old_commits,
            Map<String, List<String>> old_area
    ) {
        for (Map.Entry<String, List<String>> commits : latest_commits.entrySet()) {
            old_commits.putIfAbsent(commits.getKey(), commits.getValue());
        }
        for (Map.Entry<String, List<String>> area : latest_area.entrySet()) {
            old_area.putIfAbsent(area.getKey(), area.getValue());
        }
    }

    public static void store(
            Map<String, List<String>> commits,
            Map<String, List<String>> areas,
            Collection<String> copyright,
            Writer writer) throws IOException {
        for (String cr : copyright) {
            writer.append('#').append(' ').append(cr).append('\n').flush();
        }
        writer.append('\n');
        for (Map.Entry<String, List<String>> area : areas.entrySet()) {
            final List<String> commit = commits.get(area.getKey());
            if (commit != null) {
                for (String c : commit) {
                    writer.append('#').append(' ').append(c).append('\n').flush();
                }
            }
            for (String line : area.getValue()) {
                writer.append(line).append('\n').flush();
            }
        }
    }

    public static void parse(
            Deque<String> lines,
            Map<String, List<String>> commits,
            Map<String, List<String>> area,
            Collection<String> copyright) {
        ArrayDeque<String> commitBuffer = new ArrayDeque<>(20);
        ArrayDeque<String> dataBuffer = new ArrayDeque<>(20);
        do {
            final String s = lines.peek();
            if (s == null) break;
            String trim = s.trim();
            if (trim.isEmpty()) {
                lines.poll();
                if (copyright != null)
                    copyright.addAll(commitBuffer);
                commitBuffer.clear();
                break;
            }
            if (trim.charAt(0) == '#') {
                commitBuffer.add(trim.substring(1).trim());
                lines.poll();
                continue;
            }
            break;
        } while (true);
        do {
            do {
                String next = lines.peek();
                if (next == null) break;
                String trim = next.trim();
                if (trim.isEmpty()) {
                    lines.poll();
                    continue;
                }
                if (trim.charAt(0) == '#') {
                    commitBuffer.add(trim.substring(1).trim());
                    lines.poll();
                    continue;
                }
                break;
            } while (!lines.isEmpty());
            do {
                String next = lines.peek();
                if (next != null) {
                    if (next.isEmpty()) {
                        lines.poll();
                        dataBuffer.add(next);
                    } else {
                        if (dataBuffer.isEmpty()) {
                            lines.poll();
                            dataBuffer.add(next);
                        } else {
                            String trim = next.trim();
                            if (trim.isEmpty()) {
                                lines.poll();
                                dataBuffer.add(next);
                                continue;
                            }
                            if (trim.charAt(0) == '-') {
                                lines.poll();
                                dataBuffer.add(next);
                                continue;
                            }
                            if (!Character.isSpaceChar(next.charAt(0))) {
                                break;
                            }
                            lines.poll();
                            dataBuffer.add(next);
                            continue;
                        }
                    }
                    continue;
                }
                break;
            } while (!lines.isEmpty());
            if (dataBuffer.isEmpty()) continue;// ???
            String header = dataBuffer.peek();
            int index = header.indexOf(':');
            if (index == -1) continue; // ?????????
            String path = header.substring(0, index);
            if (commits != null && !commitBuffer.isEmpty())
                commits.put(path, new ArrayList<>(commitBuffer));
            if (area != null)
                area.put(path, new ArrayList<>(dataBuffer));
            commitBuffer.clear();
            dataBuffer.clear();
        } while (!lines.isEmpty());
    }
}
