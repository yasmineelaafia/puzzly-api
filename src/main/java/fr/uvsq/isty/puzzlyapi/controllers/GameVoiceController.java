/*
 * Puzzly - A puzzle game project
 *
 * (C) 2024 Yasmine EL AAFIA <elaafiayasmine@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Puzzly is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.uvsq.isty.puzzlyapi.controllers;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.uvsq.isty.puzzlyapi.services.AudioConverter;
import fr.uvsq.isty.puzzlyapi.services.GameVoiceService;

/**
 * A REST Controller to manage Voice control games
 * 
 * @author Yasmine EL AAFIA
 * 
 */
@RestController
@RequestMapping(value = "api/voice")
public class GameVoiceController {
    @Autowired
    private GameVoiceService gameVoiceService;

    @Autowired
    private AudioConverter audioConverter;

    /**
     * Execute commands by using voice files
     * 
     * @param file
     * @param authentication
     * @return ResponseEntity<String>
     */
    @PostMapping("/send")
    public ResponseEntity<String> receiveVoiceMessage(@RequestPart(name = "file") MultipartFile file,
            Authentication authentication) {
        File convertedFile = null;
        try {
            File sourceFile = audioConverter.convert(file);
            convertedFile = new File(authentication.getName() + "_" + file.getOriginalFilename() + ".wav");
            audioConverter.convertToWav(sourceFile, convertedFile);
            sourceFile.delete();
            String result = gameVoiceService.transferFile(convertedFile);
            convertedFile.delete();
            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch (Exception e) {
            if(convertedFile != null)
                convertedFile.delete();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
