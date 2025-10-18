// modules/customer-service/components/CustomerForm/FormSections/TargetAudienceSection.jsx
import { POST_TYPES, POST_TONES } from '../../../constants/formConstants';

export default function TargetAudienceSection({ targetAudience, updateNested, inputClass, labelClass }) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🎯</span> Hedef Kitle ve İçerik Stratejisi
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
        <div>
          <label className={labelClass}>
            Post Türü <span className="text-red-500">*</span>
          </label>
          <select
            value={targetAudience.postType}
            onChange={(e) => updateNested('targetAudience', 'postType', e.target.value)}
            required
            className={inputClass}
          >
            {POST_TYPES.map(type => (
              <option key={type.value} value={type.value}>{type.label}</option>
            ))}
          </select>
        </div>

        <div>
          <label className={labelClass}>
            Haftalık Post Sıklığı <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            min="1"
            max="7"
            value={targetAudience.postFrequency}
            onChange={(e) => updateNested('targetAudience', 'postFrequency', e.target.value)}
            required
            className={inputClass}
            placeholder="1-7 arası"
          />
        </div>

        <div>
          <label className={labelClass}>
            Post Tonu <span className="text-red-500">*</span>
          </label>
          <select
            value={targetAudience.postTone}
            onChange={(e) => updateNested('targetAudience', 'postTone', e.target.value)}
            required
            className={inputClass}
          >
            {POST_TONES.map(tone => (
              <option key={tone.value} value={tone.value}>{tone.label}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className={labelClass}>Hedef Bölge</label>
          <input
            type="text"
            value={targetAudience.targetRegion}
            onChange={(e) => updateNested('targetAudience', 'targetRegion', e.target.value)}
            className={inputClass}
            placeholder="Örn: Antalya, Lara, Muratpaşa"
          />
        </div>

        <div>
          <label className={labelClass}>Hedef Yaş Aralığı</label>
          <input
            type="text"
            value={targetAudience.audienceAge}
            onChange={(e) => updateNested('targetAudience', 'audienceAge', e.target.value)}
            className={inputClass}
            placeholder="Örn: 25-45"
          />
        </div>

        <div className="md:col-span-2">
          <label className={labelClass}>Hashtagler</label>
          <input
            type="text"
            value={targetAudience.customerHashtags}
            onChange={(e) => updateNested('targetAudience', 'customerHashtags', e.target.value)}
            className={inputClass}
            placeholder="#kahve #antalya #cafe #lara"
          />
        </div>

        <div className="md:col-span-2">
          <label className={labelClass}>Hedef Kitle İlgi Alanları</label>
          <input
            type="text"
            value={targetAudience.audienceInterests}
            onChange={(e) => updateNested('targetAudience', 'audienceInterests', e.target.value)}
            className={inputClass}
            placeholder="Örn: Kahve, Deniz, Fotograf, Sosyalleşme"
          />
        </div>
      </div>

      <div className="mt-4">
        <label className="flex items-center space-x-2 cursor-pointer">
          <input
            type="checkbox"
            checked={targetAudience.specialDates}
            onChange={(e) => updateNested('targetAudience', 'specialDates', e.target.checked)}
            className="w-5 h-5 text-indigo-600 rounded"
          />
          <span className="text-gray-700 font-medium">
            🎉 Özel günlerde otomatik post yapılsın (Bayramlar, Anneler Günü vb.)
          </span>
        </label>
      </div>
    </section>
  );
}