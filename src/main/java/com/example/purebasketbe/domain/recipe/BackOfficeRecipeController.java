package com.example.purebasketbe.domain.recipe;

import com.example.purebasketbe.domain.recipe.dto.RecipeRequestDto;
import com.example.purebasketbe.domain.recipe.dto.RecipeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@PreAuthorize("!hasAuthority('ROLE_MEMBER')")
@RequestMapping("/api/backoffice/recipes")
public class BackOfficeRecipeController {

    private final RecipeService recipeService;

    private final String ADMIN_RECIPE_PATH = "/api/backoffice/recipes";

    @GetMapping
    public ResponseEntity<Page<RecipeResponseDto>> getRecipes(@RequestParam(defaultValue = "1") int page) {
        Page<RecipeResponseDto> responseBody = recipeService.getRecipes(page - 1);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerRecipe(@RequestPart("dto") @Validated RecipeRequestDto requestDto,
                                               @RequestPart("file") MultipartFile file) {
        recipeService.registerRecipe(requestDto, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create(ADMIN_RECIPE_PATH))
                .build();
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(URI.create(ADMIN_RECIPE_PATH))
                .build();
    }
}
