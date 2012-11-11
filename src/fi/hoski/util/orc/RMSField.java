/*
 * Copyright (C) 2012 Helsingfors Segelklubb ry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.hoski.util.orc;

/**
 *
 * @author tkv
 */
public enum RMSField
{
    /**
     * Length of All
     */
    LOA,
    /**
     * IMS L
     */
    IMSL,
    /**
     * Draft
     */
    DRAFT,
    /**
     * Max Beam
     */
    BMAX,
    /**
     * Displacement
     */
    DSPL,
    /**
     * Stability Index
     */
    INDEX,
    /**
     * Dynamic Allowance
     */
    DA,
    /**
     * General Purpose Handicap
     */
    GPH,
    /**
     * Time on Time Inshore Windward/Leeward
     */
    TMF,
    /**
     * Time on Distance Inshore Windward/Leeward
     */
    ILCGA,
    /**
     * Performance Line Offshore PLT
     */ 
    PLTO,
    /**
     * Performance Line Offshore PLD
     */
    PLDO,
    /**
     * Performance Line Double handed PLT
     */
    PLT2H,
    /**
     * Performance Line Double handed PLD
     */
    PLD2H,
    /**
     * Windward / Leeward Time Allowance 6 kts
     */
    WL6,
    /**
     * Windward / Leeward Time Allowance 6 kts
     */
    WL8,
    /**
     * Windward / Leeward Time Allowance 8 kts
     */
    WL10,
    /**
     * Windward / Leeward Time Allowance 10 kts
     */
    WL12,
    /**
     * Windward / Leeward Time Allowance 12 kts
     */
    WL14,
    /**
     * Windward / Leeward Time Allowance 16 kts
     */
    WL16,
    /**
     * Windward / Leeward Time Allowance 20 kts
     */
    WL20,
    /**
     * Olympic Time Allowance 6 kts
     */
    OL6,
    /**
     * Olympic Time Allowance 8 kts
     */
    OL8,
    /**
     * Olympic Time Allowance 10 kts
     */
    OL10,
    /**
     * Olympic Time Allowance 12 kts
     */
    OL12,
    /**
     * Olympic Time Allowance 14 kts
     */
    OL14,
    /**
     * Olympic Time Allowance 16 kts
     */
    OL16,
    /**
     * Olympic Time Allowance 20 kts
     */
    OL20,
    /**
     * Circular Random Time Allowance 6 kts
     */
    CR6,
    /**
     * Circular Random Time Allowance 8 kts
     */
    CR8,
    /**
     * Circular Random Time Allowance 10 kts
     */
    CR10,
    /**
     * Circular Random Time Allowance 12 kts
     */
    CR12,
    /**
     * Circular Random Time Allowance 14 kts
     */
    CR14,
    /**
     * Circular Random Time Allowance 16 kts
     */
    CR16,
    /**
     * Circular Random Time Allowance 20 kts
     */
    CR20,
    /**
     * Non Spinnaker Time Allowance 6 kts
     */
    NSP6,
    /**
     * Non Spinnaker Time Allowance 8 kts
     */
    NSP8,
    /**
     * Non Spinnaker Time Allowance 10 kts
     */
    NSP10,
    /**
     * Non Spinnaker Time Allowance 12 kts
     */
    NSP12,
    /**
     * Non Spinnaker Time Allowance 14 kts
     */
    NSP14,
    /**
     * Non Spinnaker Time Allowance 16 kts
     */
    NSP16,
    /**
     * Non Spinnaker Time Allowance 20 kts
     */
    NSP20,
    /**
     * Ocean for PCS Time Allowance 6 kts
     */
    OC6,
    /**
     * Ocean for PCS Time Allowance 8 kts
     */
    OC8,
    /**
     * Ocean for PCS Time Allowance 10 kts
     */
    OC10,
    /**
     * Ocean for PCS Time Allowance 12 kts
     */
    OC12,
    /**
     * Ocean for PCS Time Allowance 14 kts
     */
    OC14,
    /**
     * Ocean for PCS Time Allowance 16 kts
     */
    OC16,
    /**
     * Ocean for PCS Time Allowance 20 kts
     */
    OC20,
    /**
     * Beat Angles
     */
    UA6,
    /**
     * Beat Angles
     */
    UA8,
    /**
     * Beat Angles
     */
    UA10,
    /**
     * Beat Angles
     */
    UA12,
    /**
     * Beat Angles
     */
    UA14,
    /**
     * Beat Angles
     */
    UA16,
    /**
     * Beat Angles
     */
    UA20,
    /**
     * Gybe Angles
     */
    DA6,
    /**
     * Gybe Angles
     */
    DA8,
    /**
     * Gybe Angles
     */
    DA10,
    /**
     * Gybe Angles
     */
    DA12,
    /**
     * Gybe Angles
     */
    DA14,
    /**
     * Gybe Angles
     */
    DA16,
    /**
     * Gybe Angles
     */
    DA20,
    /**
     * Beat VMG
     */
    UP6,
    /**
     * Beat VMG
     */
    UP8,
    /**
     * Beat VMG
     */
    UP10,
    /**
     * Beat VMG
     */
    UP12,
    /**
     * Beat VMG
     */
    UP14,
    /**
     * Beat VMG
     */
    UP16,
    /**
     * Beat VMG
     */
    UP20,
    /**
     * Time Allowance Angle-Speed
     */
    R526,
    /**
     * Time Allowance Angle-Speed
     */
    R528,
    /**
     * Time Allowance Angle-Speed
     */
    R5210,
    /**
     * Time Allowance Angle-Speed
     */
    R5212,
    /**
     * Time Allowance Angle-Speed
     */
    R5214,
    /**
     * Time Allowance Angle-Speed
     */
    R5216,
    /**
     * Time Allowance Angle-Speed
     */
    R5220,
    /**
     * Time Allowance Angle-Speed
     */
    R606,
    /**
     * Time Allowance Angle-Speed
     */
    R608,
    /**
     * Time Allowance Angle-Speed
     */
    R6010,
    /**
     * Time Allowance Angle-Speed
     */
    R6012,
    /**
     * Time Allowance Angle-Speed
     */
    R6014,
    /**
     * Time Allowance Angle-Speed
     */
    R6016,
    /**
     * Time Allowance Angle-Speed
     */
    R6020,
    /**
     * Time Allowance Angle-Speed
     */
    R756,
    /**
     * Time Allowance Angle-Speed
     */
    R758,
    /**
     * Time Allowance Angle-Speed
     */
    R7510,
    /**
     * Time Allowance Angle-Speed
     */
    R7512,
    /**
     * Time Allowance Angle-Speed
     */
    R7514,
    /**
     * Time Allowance Angle-Speed
     */
    R7516,
    /**
     * Time Allowance Angle-Speed
     */
    R7520,
    /**
     * Time Allowance Angle-Speed
     */
    R906,
    /**
     * Time Allowance Angle-Speed
     */
    R908,
    /**
     * Time Allowance Angle-Speed
     */
    R9010,
    /**
     * Time Allowance Angle-Speed
     */
    R9012,
    /**
     * Time Allowance Angle-Speed
     */
    R9014,
    /**
     * Time Allowance Angle-Speed
     */
    R9016,
    /**
     * Time Allowance Angle-Speed
     */
    R9020,
    /**
     * Time Allowance Angle-Speed
     */
    R1106,
    /**
     * Time Allowance Angle-Speed
     */
    R1108,
    /**
     * Time Allowance Angle-Speed
     */
    R11010,
    /**
     * Time Allowance Angle-Speed
     */
    R11012,
    /**
     * Time Allowance Angle-Speed
     */
    R11014,
    /**
     * Time Allowance Angle-Speed
     */
    R11016,
    /**
     * Time Allowance Angle-Speed
     */
    R11020,
    /**
     * Time Allowance Angle-Speed
     */
    R1206,
    /**
     * Time Allowance Angle-Speed
     */
    R1208,
    /**
     * Time Allowance Angle-Speed
     */
    R12010,
    /**
     * Time Allowance Angle-Speed
     */
    R12012,
    /**
     * Time Allowance Angle-Speed
     */
    R12014,
    /**
     * Time Allowance Angle-Speed
     */
    R12016,
    /**
     * Time Allowance Angle-Speed
     */
    R12020,
    /**
     * Time Allowance Angle-Speed
     */
    R1356,
    /**
     * Time Allowance Angle-Speed
     */
    R1358,
    /**
     * Time Allowance Angle-Speed
     */
    R13510,
    /**
     * Time Allowance Angle-Speed
     */
    R13512,
    /**
     * Time Allowance Angle-Speed
     */
    R13514,
    /**
     * Time Allowance Angle-Speed
     */
    R13516,
    /**
     * Time Allowance Angle-Speed
     */
    R13520,
    /**
     * Time Allowance Angle-Speed
     */
    R1506,
    /**
     * Time Allowance Angle-Speed
     */
    R1508,
    /**
     * Time Allowance Angle-Speed
     */
    R15010,
    /**
     * Time Allowance Angle-Speed
     */
    R15012,
    /**
     * Time Allowance Angle-Speed
     */
    R15014,
    /**
     * Time Allowance Angle-Speed
     */
    R15016,
    /**
     * Time Allowance Angle-Speed
     */
    R15020,
    /**
     * Run VMG
     */
    D6,
    /**
     * Run VMG
     */
    D8,
    /**
     * Run VMG
     */
    D10,
    /**
     * Run VMG
     */
    D12,
    /**
     * Run VMG
     */
    D14,
    /**
     * Run VMG
     */
    D16,
    /**
     * Run VMG
     */
    D20,
    /**
     * Triple Number Offshore Low
     */
    OTNLOW,
    /**
     * Triple Number Offshore Medium
     */
    OTNMED,
    /**
     * Triple Number Offshore High
     */
    OTNHIG,
    /**
     * Triple Number Inshore Low
     */
    ITNLOW,
    /**
     * Triple Number Inshore Medium
     */
    ITNMED,
    /**
     * Triple Number Inshore High
     */
    ITNHIG,
    /**
     * Double Handed ToD
     */
    DH_TOD,
    /**
     * Double Handed ToT
     */
    DH_TOT,
    /**
     * Performance Line Inshore PLT
     */ 
    PLTI,
    /**
     * Performance Line Inshore PLD
     */ 
    PLDI,
    /**
     * DISPLACEMENT (WEIGHT) SAILING TRIM
     */
    DSPS,
    /**
     * WETTED SURFACE
     */
    WSS,
    /**
     * Mainsail
     */
    MAIN,
    /**
     * Jib / Genoa
     */
    GENOA,
    /**
     * Symmetric Spinnaker
     */
    SYM,
    /**
     * Asymmetric Spinnaker
     */
    ASYM,
    /**
     * Time On Time Offshore
     */
    TMFOF;
    
    public static RMSField correctedValueOf(String name)
    {
        if ("PLT-O".equals(name))
        {
            return PLTO;
        }
        if ("PLD-O".equals(name))
        {
            return PLDO;
        }
        if ("PLT-I".equals(name))
        {
            return PLTI;
        }
        if ("PLD-I".equals(name))
        {
            return PLDI;
        }
        if ("TMF-OF".equals(name))
        {
            return TMFOF;
        }

        return valueOf(name);
    }

}
