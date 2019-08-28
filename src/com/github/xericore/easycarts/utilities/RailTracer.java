package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.data.RailTrace;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;

import java.util.ArrayList;
import java.util.List;

public class RailTracer
{
    private List<Rail.Shape> _tracedRailShapes = new ArrayList<>();
    private int _traceLength;

    public List<Rail.Shape> traceRails(Block block, BlockFace facing, int traceLength)
    {
        _traceLength = traceLength;

        if (addCurrentRail(block) == false)
            return _tracedRailShapes;

        traceNextRailRecursive(block, facing);

        return _tracedRailShapes;
    }

    private boolean addCurrentRail(Block block)
    {
        if(block.getBlockData().getMaterial() != Material.RAIL)
            return false;

        Rail currentRail = (Rail) block.getBlockData();

        _tracedRailShapes.add(currentRail.getShape());
        return true;
    }

    private void traceNextRailRecursive(Block block, BlockFace facing)
    {
        _traceLength--;

        while (_traceLength > 0)
        {
            if(facing == null || block == null)
            {
                _traceLength = 0;
                return;
            }

            RailTrace nextTrace = new RailTrace(block, facing);

            _tracedRailShapes.add(nextTrace.getNextRailShape());

            traceNextRailRecursive(nextTrace.getNextBlock(), nextTrace.getNextFacing());
        }
    }
}
