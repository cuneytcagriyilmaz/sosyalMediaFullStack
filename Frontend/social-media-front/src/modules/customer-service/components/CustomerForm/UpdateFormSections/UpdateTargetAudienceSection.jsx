// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateTargetAudienceSection.jsx
import { POST_TYPES, POST_TONES } from '../../../constants/formConstants';
import { SaveButton } from '../FormComponents';

export default function UpdateTargetAudienceSection({ 
  targetAudience, 
  updateNested, 
  onSave,
  loading,
  inputClass, 
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
      <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
        <span className="mr-2">🎯</span> Hedef Kitle
      </h3>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4 mb-4">
        <div>
          <label className={labelClass}>
            Post Türü <span className="text-red-500">*</span>
          </label>
          <select
            value={targetAudience.postType || ""}
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
            value={targetAudience.postFrequency || ""}
            onChange={(e) => updateNested('targetAudience', 'postFrequency', e.target.value)}
            required
            className={inputClass}
            placeholder="1-7"
          />
        </div>

        <div>
          <label className={labelClass}>
            Post Tonu <span className="text-red-500">*</span>
          </label>
          <select
            value={targetAudience.postTone || ""}
            onChange={(e) => updateNested('targetAudience', 'postTone', e.target.value)}
            required
            className={inputClass}
          >
            {POST_TONES.map(tone => (
              <option key={tone.value} value={tone.value}>{tone.label}</option>
            ))}
          </select>
        </div>

        <div>
          <label className={labelClass}>Hedef Bölge</label>
          <input
            type="text"
            value={targetAudience.targetRegion || ""}
            onChange={(e) => updateNested('targetAudience', 'targetRegion', e.target.value)}
            className={inputClass}
            placeholder="Örn: Antalya"
          />
        </div>

        <div>
          <label className={labelClass}>Hedef Yaş</label>
          <input
            type="text"
            value={targetAudience.audienceAge || ""}
            onChange={(e) => updateNested('targetAudience', 'audienceAge', e.target.value)}
            className={inputClass}
            placeholder="Örn: 25-45"
          />
        </div>

        <div className="sm:col-span-2 lg:col-span-3">
          <label className={labelClass}>Hashtagler</label>
          <input
            type="text"
            value={targetAudience.customerHashtags || ""}
            onChange={(e) => updateNested('targetAudience', 'customerHashtags', e.target.value)}
            className={inputClass}
            placeholder="#kahve #antalya"
          />
        </div>

        <div className="sm:col-span-2 lg:col-span-3">
          <label className={labelClass}>İlgi Alanları</label>
          <input
            type="text"
            value={targetAudience.audienceInterests || ""}
            onChange={(e) => updateNested('targetAudience', 'audienceInterests', e.target.value)}
            className={inputClass}
            placeholder="Örn: Kahve, Deniz"
          />
        </div>
      </div>

      <div className="mb-4">
        <label className="flex items-center space-x-2 cursor-pointer">
          <input
            type="checkbox"
            checked={targetAudience.specialDates || false}
            onChange={(e) => updateNested('targetAudience', 'specialDates', e.target.checked)}
            className="w-5 h-5 text-indigo-600 rounded"
          />
          <span className="text-gray-700 font-medium text-sm sm:text-base">
            🎉 Özel günlerde post
          </span>
        </label>
      </div>

      <SaveButton onClick={onSave} loading={loading} />
    </section>
  );
}