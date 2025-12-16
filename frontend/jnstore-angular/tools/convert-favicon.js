const sharp = require('sharp');
const pngToIcoModule = require('png-to-ico');
const pngToIco = pngToIcoModule && (pngToIcoModule.default || pngToIcoModule);
const fs = require('fs');
const path = require('path');

async function run() {
  const projectRoot = path.resolve(__dirname, '..');
  const input = path.join(projectRoot, 'public', 'logo_jn_store.jpg');
  const output = path.join(projectRoot, 'public', 'favicon.ico');

  if (!fs.existsSync(input)) {
    console.error('Input logo not found:', input);
    process.exit(1);
  }

  const sizes = [16, 24, 32, 48, 64, 128, 256];
  try {
    const pngBuffers = [];
    for (const s of sizes) {
      const buf = await sharp(input).resize(s, s, { fit: 'cover' }).png().toBuffer();
      pngBuffers.push(buf);
    }

    const icoBuffer = await pngToIco(pngBuffers);
    fs.writeFileSync(output, icoBuffer);
    console.log('Generated favicon:', output);
  } catch (err) {
    console.error('Error generating favicon:', err);
    process.exit(2);
  }
}

run();
