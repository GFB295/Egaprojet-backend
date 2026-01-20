Write-Host "Initialisation de l'utilisateur admin..." -ForegroundColor Green
Write-Host ""
Write-Host "Username: admin" -ForegroundColor Yellow
Write-Host "Password: Admin@123" -ForegroundColor Yellow
Write-Host ""

# Attendre un peu pour s'assurer que le backend est prêt
Write-Host "Attente de 5 secondes..." -ForegroundColor Gray
Start-Sleep -Seconds 5

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/init-admin?username=admin&password=Admin@123" -Method POST
    Write-Host $response -ForegroundColor Green
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "Admin cree avec succes !" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Vous pouvez maintenant vous connecter avec:" -ForegroundColor Cyan
    Write-Host "  Username: admin" -ForegroundColor White
    Write-Host "  Password: Admin@123" -ForegroundColor White
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 400) {
        Write-Host "L'admin existe deja dans la base de donnees." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Vous pouvez utiliser:" -ForegroundColor Cyan
        Write-Host "  Username: admin" -ForegroundColor White
        Write-Host "  Password: Admin@123" -ForegroundColor White
    } else {
        Write-Host "Erreur: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Assurez-vous que:" -ForegroundColor Yellow
        Write-Host "  1. Le backend est demarre sur http://localhost:8080" -ForegroundColor Yellow
        Write-Host "  2. MongoDB est en cours d'execution" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Reessayez dans quelques secondes..." -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "Appuyez sur une touche pour continuer..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
